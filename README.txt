In order to keep this git repository small, it does not include the
required libsdl2, so you must manually add it (and create the symbolic
link shown below) after cloning:

    cd ~
    git clone https://github.com/artcfox/cuzebox-android
    cd ~/Downloads
    wget http://libsdl.org/release/SDL2-2.0.6.tar.gz
    cd ~/cuzebox-android/android-project/app/src/main/jni/
    tar xf ~/Downloads/SDL2-2.0.6.tar.gz
    mv SDL2-2.0.6 SDL2
    cd SDL2/include
    ln -s . SDL2

After following the above instructions, you should be able to open the
cuzebox-android/android-project directory with Android Studio and
generate an .apk file.

This project requires that you have Android Studio and the NDK
installed.


Troubleshooting:

If you are having trouble getting the Android Virtual Device (AVD) to
run on Debian, then install the following packages:

    sudo apt-get install lib32stdc++6 lib32z1 lib32ncurses5

If it still doesn't run, then configure the AVD to use Software
Rendering.
