package ru.lab.game.entity;

public abstract class Creature extends Entity{

    protected Integer speed;
    protected Integer health;

    public abstract void makeMove();

}
