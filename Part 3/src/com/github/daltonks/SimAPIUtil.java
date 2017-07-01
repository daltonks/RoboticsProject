package com.github.daltonks;

import coppelia.remoteApi;

import java.util.function.Supplier;

public class SimAPIUtil {
    public static <T> T handleResponse(int code, Supplier<T> onSuccess) {
        return handleResponse(code, onSuccess, () -> null);
    }

    public static <T> T handleResponse(int code, Supplier<T> onSuccess, Supplier<T> onError) {
        if (code == remoteApi.simx_return_ok) {
            return onSuccess.get();
        } else {
            System.out.format("Remote API function call returned with error code: %d\n", code);
            return onError.get();
        }
    }

    public static void handleResponse(int code) {
        handleResponse(code, () -> { });
    }

    public static void handleResponse(int code, Runnable onSuccess) {
        handleResponse(code, onSuccess, () -> { });
    }

    public static void handleResponse(int code, Runnable onSuccess, Runnable onError) {
        if (code == remoteApi.simx_return_ok) {
            onSuccess.run();
        } else {
            System.out.format("Remote API function call returned with error code: %d\n", code);
            onError.run();
        }
    }
}