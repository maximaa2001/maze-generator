package com.maks.mazegenerator.entity;

import com.maks.mazegenerator.MainApplication;
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

    protected AnimationHero(double imageViewWidth, double imageViewHeight) {
        images = initImages();
        this.imageView = new ImageView();
        imageView.setFitWidth(imageViewWidth);
        imageView.setFitHeight(imageViewHeight);
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

    public ImageView getImageView() {
        return imageView;
    }
}
