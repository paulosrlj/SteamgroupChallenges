package com.steamgroups.backendchallenge.beans;

import com.steamgroups.backendchallenge.model.Character;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Named
@RequestScoped
public class DataTableBean {
    private List<Character> characters;

    @PostConstruct
    public void init() {
        characters = new ArrayList<>(ChartBean.characters);
        setCharacters(characters.stream().filter(Character::isPilotedMillenumFalcon).collect(Collectors.toList()));
    }

    public List<Character> getCharacters() {
        return characters;
    }

    public void setCharacters(List<Character> characters) {
        this.characters = characters;
    }
}

