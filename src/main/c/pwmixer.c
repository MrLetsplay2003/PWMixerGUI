#include <stdio.h>
#include <stdlib.h>
#include <stdint.h>

#include "me_mrletsplay_pwmixer_PWMixer.h"
#include "pwmixer.h"

JNIEXPORT void JNICALL Java_me_mrletsplay_pwmixer_PWMixer_sysConnect(JNIEnv *env, jclass clz, jobjectArray argv) {
	jsize argc = (*env)->GetArrayLength(env, argv);

	jstring *strings = malloc(argc * sizeof(jstring *));
	char **argvVal = malloc(argc * sizeof(char *));
	for(jsize i = 0; i < argc; i++) {
		jstring str = (*env)->GetObjectArrayElement(env, argv, i);
		strings[i] = str;
		argvVal[i] = (*env)->GetStringUTFChars(env, str, NULL);
	}

	pwm_sysConnect(&argc, &argvVal);

	for(jsize i = 0; i < argc; i++) {
		(*env)->ReleaseStringUTFChars(env, strings[i], argvVal[i]);
	}
}

JNIEXPORT void JNICALL Java_me_mrletsplay_pwmixer_PWMixer_sysDisconnect(JNIEnv *env, jclass clz){
	pwm_sysDisconnect();
}

JNIEXPORT jboolean JNICALL Java_me_mrletsplay_pwmixer_PWMixer_sysIsRunning(JNIEnv *env, jclass clz){
	return pwm_sysIsRunning();
}

JNIEXPORT void JNICALL Java_me_mrletsplay_pwmixer_PWMixer_ioConnect0(JNIEnv *env, jclass clz, jlong input, jlong output) {
	pwm_IO *in = pwm_ioGetByID(input);
	pwm_IO *out = pwm_ioGetByID(output);
	pwm_ioConnect(in, out);
}

JNIEXPORT void JNICALL Java_me_mrletsplay_pwmixer_PWMixer_ioDisconnect0(JNIEnv *env, jclass clz, jlong input, jlong output) {
	pwm_IO *in = pwm_ioGetByID(input);
	pwm_IO *out = pwm_ioGetByID(output);
	pwm_ioDisconnect(in, out);
}

JNIEXPORT jlong JNICALL Java_me_mrletsplay_pwmixer_PWMixer_ioCreateInput0(JNIEnv *env, jclass clz, jstring name, jboolean isSink) {
	const char *nameChars = (*env)->GetStringUTFChars(env, name, NULL);
	pwm_IO *in = pwm_ioCreateInput(nameChars, isSink);

	(*env)->ReleaseStringUTFChars(env, name, nameChars);
	return pwm_ioGetID(in);
}

JNIEXPORT void JNICALL Java_me_mrletsplay_pwmixer_PWMixer_ioDestroyInput0(JNIEnv *env, jclass clz, jlong id) {
	pwm_IO *obj = pwm_ioGetByID(id);
	pwm_ioDestroy(obj);
}

JNIEXPORT jlong JNICALL Java_me_mrletsplay_pwmixer_PWMixer_ioCreateOutput0(JNIEnv *env, jclass clz, jstring name, jboolean isSource) {
	const char *nameChars = (*env)->GetStringUTFChars(env, name, NULL);
	pwm_IO *in = pwm_ioCreateOutput(nameChars, isSource);

	(*env)->ReleaseStringUTFChars(env, name, nameChars);
	return pwm_ioGetID(in);
}

JNIEXPORT void JNICALL Java_me_mrletsplay_pwmixer_PWMixer_ioDestroyOutput0(JNIEnv *env, jclass clz, jlong id) {
	pwm_IO *obj = pwm_ioGetByID(id);
	pwm_ioDestroy(obj);
}

JNIEXPORT void JNICALL Java_me_mrletsplay_pwmixer_PWMixer_ioSetVolume0(JNIEnv *env, jclass clz, jlong id, jfloat volume) {
	pwm_IO *obj = pwm_ioGetByID(id);
	pwm_ioSetVolume(obj, volume);
}

JNIEXPORT void JNICALL Java_me_mrletsplay_pwmixer_PWMixer_ioSetConnectionVolume0(JNIEnv *env, jclass clz, jlong input, jlong output, jfloat volume) {
	pwm_IO *in = pwm_ioGetByID(input);
	pwm_IO *out = pwm_ioGetByID(output);
	pwm_ioSetConnectionVolume(in, out, volume);
}

JNIEXPORT jfloat JNICALL Java_me_mrletsplay_pwmixer_PWMixer_ioGetLastVolume0(JNIEnv *env, jclass clz, jlong id) {
	pwm_IO *obj = pwm_ioGetByID(id);
	return pwm_ioGetLastVolume(obj);
}

JNIEXPORT void JNICALL Java_me_mrletsplay_pwmixer_PWMixer_debugEnableLog(JNIEnv *env, jclass clz, jboolean log) {
	pwm_debugEnableLog(log);
}

JNIEXPORT jboolean JNICALL Java_me_mrletsplay_pwmixer_PWMixer_debugIsLogEnabled(JNIEnv *env, jclass clz) {
	return pwm_debugIsLogEnabled();
}
