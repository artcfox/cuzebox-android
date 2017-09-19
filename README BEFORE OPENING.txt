Install Android Studio, and the NDK.

Then follow the instructions below to setup the libsdl dependency


@ BEFORE YOU OPEN
@ THE PROJECT WITH
@ ANDROID STUDIO:


    cd ~/Downloads
    wget http://libsdl.org/release/SDL2-2.0.5.tar.gz
    cd android-project/app/src/main/jni/
    tar xf ~/Downloads/SDL2-2.0.5.tar.gz
    mv SDL2-2.0.5 SDL2
    cd SDL2/include
    ln -s . SDL2

Now open it with Android Studio and you should be able to generate an .apk file,
but IT WILL IMMEDIATELY CRASH UNLESS you follow the steps below for running it.


@ BEFORE RUNNING CUZEBOX
@ ON YOUR ANDROID DEVICE:


After enabling developer mode, then enabling install from untrusted sources,
you must drag the app icon to the App Info part of your home screen, and then
click on Permissions, and then slide the slider over to enable the Storage
permission.

This version of CUzeBox for Android requires you to create a folder called:

    /storage/emulated/0/cuzebox

on your Android device, and copy the Bootloader_0_4_5.hex file along with
any .uze files you want to be able to play into that folder.
