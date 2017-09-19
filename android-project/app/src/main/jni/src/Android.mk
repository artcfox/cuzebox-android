LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE := main

SDL_PATH := ../SDL2

LOCAL_C_INCLUDES := $(LOCAL_PATH)/$(SDL_PATH)/include

LOCAL_CFLAGS := -DFLAG_SELFCONT=0 -DFLAG_NOCONSOLE=1 -DFLAG_DISPLAY_GAMEONLY=1 -DVER_DATE=0x20170915 -DFLAG_DISPLAY_FRAMEMERGE=1

# Add your application source files here...
LOCAL_SRC_FILES := $(SDL_PATH)/src/main/android/SDL_android_main.c \
	cuzebox/audio.c \
	cuzebox/chars.c \
	cuzebox/conout.c \
	cuzebox/cu_avr.c \
	cuzebox/cu_avrc.c \
	cuzebox/cu_avrfg.c \
	cuzebox/cu_ctr.c \
	cuzebox/cu_hfile.c \
	cuzebox/cu_spi.c \
	cuzebox/cu_spisd.c \
	cuzebox/cu_spir.c \
	cuzebox/cu_ufile.c \
	cuzebox/cu_vfat.c \
	cuzebox/eepdump.c \
	cuzebox/filesys.c \
	cuzebox/frame.c \
	cuzebox/ginput.c \
	cuzebox/guicore.c \
	cuzebox/main.c \
	cuzebox/textgui.c

LOCAL_SHARED_LIBRARIES := SDL2

LOCAL_LDLIBS := -lGLESv1_CM -lGLESv2 -llog

include $(BUILD_SHARED_LIBRARY)
