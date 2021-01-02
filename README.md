# Diary/WeatherApplication
A simple application for making small diary inputs with the added benefit of getting temperature at the time of posting.

At its final polished form this app wouldn't get much larger in complexity as it is right now. This is why we opted to go for an MVC approach when choosing design pattern. The MVC structure suited us very well because of the apps small footprint which wouldn't benefit from the greater modularity that MVVM or even MVP offers.

To be able to offer a very smooth and responsive app we took the one big workload and segmented it into an offline mode as well as online mode through the means of caching. By caching the rather small Diary inputs we cuold now offer it even if the user somehow lost internet connection and by trheading the actual gathering and displaying of the data the main thread stays unhindered and clean.

The app further implements usage of unit tests specifically testing the user input fields for easier possible future expansion.

## The caching process of the application
We try to use the power of caching while still making sure to always have up to date information in the cache.
The application can be run in two modes, one is offline mode and the other is online mode.

If the application runs in offline mode, the main activity will provide the user with diary inputs from the cache.

### Caching (Offline mode)

#### *MainActivity*


 
    // If there is no network available, try to populate the recyclerview based on the cache file.
    if (!isConnected)
    {
        try {
    
            val diaryInputsList = cacheHelper.readCachedFile(this)
            populateTheRecyclerView(diaryInputsList, false)
    
        }catch (e: FileNotFoundException){
        Log.d(TAG, "Cache file not found", e)
        }
    }
    
    
When the user wants to add new inputs to the diary and proceeds to the AddPostActivity, we look for existing cache files to add inputs to it, or we create a new cache file if there is no existing.

#### *AddPostActivity*

   
    if (checkForCacheFile()) {
    
        val diaryInputsList = mutableListOf<PostFirestore>()
    
        task.temp = tempText
        task.diaryInput = postEditText.text.toString()
        task.creationDate = currentDate
    
        diaryInputsList.add(task)
    
        cacheHelper.addToCacheFile(this, diaryInputsList)

        firestoreHelper.addToFirestore(task)
        finish()
    
    } 

The diaryInputsList will be added to an existing cache file through the CacheModel.

#### *CacheModel*


    override fun addToCacheFile(context: Context, diaryInputsList: MutableList<PostFirestore>) {

        val diaryListFromCache = readCachedFile(context)

        //OutputStream that we will write our combined list to
        val outputFile = File(context.cacheDir, "cache").toString() + ".tmp"
        val out = ObjectOutputStream(FileOutputStream(File(outputFile)))

        val combinedList = diaryInputsList + diaryListFromCache

        out.writeObject(combinedList)
        out.close()


    }                   
                    
  

### Caching (Online mode)
If there is network available, we try to get data from firestore and we update our cache to make sure that it's up to date.

#### *MainActivity*

    if (isConnected)
            {
                // If there is a network available, get firestore data and populate the RV based on that.
                getFirestoreToRV()
            }
            
            
            private fun getFirestoreToRV() {
                    val firestoreModel = PostFirestoreModel()
                    firestoreModel.getFromFirestore(this) { list ->
                        populateTheRecyclerView(list, true)
                    }
                }
                
We use the firestoreModel to return a up to date list that will be used to populate our recyclerview.

#### *PostFirestoreModel*


    override fun getFromFirestore(context: Context, callback: (MutableList<PostFirestore>) -> Unit) {

        val diaryInputsList = mutableListOf<PostFirestore>()

        try {
            db.collection("DiaryInputs")
                    .addSnapshotListener { snapshot, e ->
                        diaryInputsList.clear()

                        if (snapshot != null && !snapshot.isEmpty) {
                            for (doc in snapshot.documents) {
                                val diaryInputs = doc.toObject(PostFirestore::class.java)

                                // Adding data from firestore to out mutableList
                                diaryInputsList.add(diaryInputs!!)
                            }
                            // Creating our cache file (or overwriting existing), with fresh data from firestore.
                            cacheHelper.createCachedFile(context, diaryInputsList)

                            // Returning the up to date mutableList
                            callback(diaryInputsList)

                        } else {
                            //Refreshing the RV and deleting the cache if firestore is empty.
                            cacheHelper.deleteCachedFile(context)
                            callback(diaryInputsList)
                        }
                    }
        } catch (e: Exception){
            Log.d(TAG, "Failure", e)
        }
    }
                
                
                

               
