package org.uzebox.cuzebox.app;

import org.libsdl.app.SDLActivity; 

/**
 * A sample wrapper class that just calls SDLActivity 
 */ 

public class CUzeBoxActivity extends SDLActivity { 
  
 @Override
    protected void onCreate(Bundle savedInstanceState) {

        if(Build.VERSION.SDK_INT>=23) {
            //did user granted this ermission?
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                //nope, it's Chuck Testa

                //should we show toast with info WHY do we ned this permission form user?
                if(shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    final Toast info =  Toast.makeText(this, "Do it, albo urwę ci ryja", Toast.LENGTH_LONG);
                }
                //show this little funny popup where user can grant us unlimited power!
                //dunno why 13, it always worked though :P
                requestPermissions(new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, 13);
            }

        }
      
        //load SDLActivity constructor as usual
        super.onCreate(savedInstanceState);
    }
}
}
