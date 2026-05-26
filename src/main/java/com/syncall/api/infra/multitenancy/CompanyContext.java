package com.syncall.api.infra.multitenancy;

public class CompanyContext {

    private static final ThreadLocal<Long> currentCompany = new ThreadLocal<>();

    public static void setCompanyId(Long id){
        currentCompany.set(id);
    }

    public static Long getCompanyId(){
        return currentCompany.get();
    }

    public static void clear(){
        currentCompany.remove();
    }

}
