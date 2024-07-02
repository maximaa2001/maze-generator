package com.maks.mazegenerator.entity;

import com.maks.mazegenerator.constant.AppConstant;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.util.Duration;

import java.util.*;

public class Pacman extends AnimationHero {

    @Override
    protected List<Image> initImages() {
        Image image1 = createImageView(getResourcePath(AppConstant.PACMAN_1_IMAGE));
        Image image2 = createImageView(getResourcePath(AppConstant.PACMAN_2_IMAGE));
        return List.of(image1, image2);
    }

    @Override
    public void nextFrame() {
        Image image = images.get(currentImage);
        currentImage = (currentImage == 0) ? 1 : 0;
        imageView.setImage(image);
        Rectangle2D sprite = new Rectangle2D(0, 0, 348, 348);
        imageView.setViewport(sprite);
    }

    @Override
    public Duration getFrameDuration() {
        return Duration.millis(250);
    }
}
