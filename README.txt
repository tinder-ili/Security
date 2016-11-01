Compile the app from android studio and install it on your device.

1. First time user open the app, grant permission to Camera and external storage.
2. Hit take photo button, will take to camera view. And 10 photo will be taken.
3. After photo taken is done, will go to photo preview view.
4. You will be able to preview the photos.
5. Input pin to save the photos. Pin needs to be longer than 8 characters.
6. After photo is saved, navigate back to the main view, a "Facial Recognition" Button will show.
7. Hit it will print out the log for retrieving each file for decryption, and compare the file hash. If all the files passed the decryption and hash check, the result will be success. Otherwise will abort.

How I store the file:
1. A file on the external storage will be created for each photo.
2. A db object Image will be created. The photo uid and file url will be saved to db.
3. A db object ImageHash will be created, the photo uid and a hash string based on the photo content will be saved.
4. Photo content will be encrypted with user's pin and saved into the file.
5. User's pin will also be encrypted and save into db, with a predefined key.

Some notes:
1. ImageHash is used to verify the photo file didn't get hacked or replaced. I
2. Saved user pin shared preference, but this should be an API post.
3. The app manifest is set to allowBackup="false", so the data can not be cloned(for none rooted device).

Challenges & Bugs:
1. Was going to save the image encrypted bytes into db, but for simplicity i was using ActiveAndroid, which is an Android ORM lib. When i was testing, i realize ActiveAndroid does not support BLOB type, I can not save byte array. So I created file for storage and save the file pathh to db.

2. I have bugs when comparing the hash. Not every time after photo is done taken, do the facial recognition will pass. It is always due to one of the file hash did not match. I didn't have enough time look into it. Will definitely do better for this.

3. Store the key locally is definitely not ideal. But sending over server and retrieve it each time probably is not ideal either. I am not sure what is the best approach for this yet. 

4. The camera preview and photo taken part is flaky. I will definitely spend more time to make it better UI and robust.

5. I use Glide to load the byte[] to image. It does the bitmap caching. But i could have a background thread to process each file create bitmap for thumbnails for preview. Better memory management.
