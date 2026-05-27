package com.syncall.api.infra.multitenancy;

public class UserContext {

    private static final ThreadLocal<Long> currentUser = new ThreadLocal<>();

    public static void setUserId(Long id){
        currentUser.set(id);
    }

    public static Long getUserId(){
        return currentUser.get();
    }

    public static void clear(){
        currentUser.remove();
    }

}
