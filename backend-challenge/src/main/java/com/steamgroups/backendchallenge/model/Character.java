package com.steamgroups.backendchallenge.model;

import java.util.List;

public class Character {
    private String name;
    private List<String> films;
    private List<String> starships;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getNumberOfTimesThatAppeared() {
        return films.size();
    }

    public List<String> getFilms() {
        return films;
    }

    public void setFilms(List<String> films) {
        this.films = films;
    }

    public List<String> getStarships() {
        return starships;
    }

    public void setStarships(List<String> starships) {
        this.starships = starships;
    }

    public boolean isPilotedMillenumFalcon() {
        for(String s : starships) {
            if (s.contains("10"))
                return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "Character{" +
                "name='" + name + '\'' +
                ", films=" + films +
                ", starships=" + starships +
                '}';
    }
}
//"name": "Luke Skywalker",
//
//        "films": [
//        "https://swapi.dev/api/films/1/",
//        "https://swapi.dev/api/films/2/",
//        "https://swapi.dev/api/films/3/",
//        "https://swapi.dev/api/films/6/"
//        ],
//
//        "starships": [
//        "https://swapi.dev/api/starships/12/",
//        "https://swapi.dev/api/starships/22/"
//        ],
