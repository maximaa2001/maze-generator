package com.maks.mazegenerator.entity;

import com.maks.mazegenerator.MainApplication;
import javafx.animation.PathTransition;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public abstract class AnimationHero {
    protected final ImageView imageView;
    protected final List<Image> images;
    protected int currentImage = 0;
    private Timeline timeline;
    private PathTransition pathTransition;

    protected AnimationHero() {
        images = initImages();
        this.imageView = new ImageView();
    }

    protected String getResourcePath(String path) {
        return Objects.requireNonNull(MainApplication.class.getResource(path)).getPath();
    }

    protected Image createImageView(String path) {
        try (InputStream is = new FileInputStream(path)) {
            return new Image(is);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    protected abstract List<Image> initImages();

    public Duration getFrameDuration() {
        return Duration.seconds(1);
    }

    public abstract void nextFrame();

    public Timeline getTimeline() {
        return timeline;
    }

    public void setTimeline(Timeline timeline) {
        this.timeline = timeline;
    }

    public PathTransition getPathTransition() {
        return pathTransition;
    }

    public void setPathTransition(PathTransition pathTransition) {
        this.pathTransition = pathTransition;
    }

    public ImageView getImageView() {
        return imageView;
    }
}
