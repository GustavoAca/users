package com.glaiss.users.infra.client.oauth;

import com.glaiss.core.providers.ApplicationContextProvider;

public enum TipoOauth {

    GOOGLE(OauthGoogleServiceImpl.class),
    GITHUB(OauthGithubServiceImpl.class);

    private final Class<? extends OauthService> serviceClass;

    TipoOauth(Class<? extends OauthService> serviceClass) {
        this.serviceClass = serviceClass;
    }

    public OauthService getService() {
        return ApplicationContextProvider.getBean(serviceClass);
    }
}
