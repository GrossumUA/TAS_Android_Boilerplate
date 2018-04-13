package com.theappsolutions.boilerplate.data.model.realm;

import com.theappsolutions.boilerplate.data.model.api.ProjectsResponse;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class CachedProject extends RealmObject {

    @PrimaryKey
    private long id;

    private String name;

    private String type;

    private String logoSrc;

    public CachedProject() {
    }

    public CachedProject(long id, String name, String type, String logoSrc) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.logoSrc = logoSrc;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getLogoSrc() {
        return logoSrc;
    }

    public static CachedProject fromProjectResponse(ProjectsResponse projectsResponse) {
        return new CachedProject(projectsResponse.getId(), projectsResponse.getName(),
                projectsResponse.getType(), projectsResponse.getLogoSrc());
    }
}
