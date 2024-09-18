package com.kropsz.mslastfm.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

import javax.imageio.ImageIO;

import org.springframework.stereotype.Component;

import com.kropsz.mslastfm.dto.album.AlbumsResponse;
import com.kropsz.mslastfm.exception.ImageCreateException;

@Component
public class CollageConstructor {

    public static final int GRID_WIDTH = 1250;
    public static final int GRID_HEIGHT = 1250;

    private CollageData albums;
    private final Map<String, BufferedImage> imageCache = new ConcurrentHashMap<>();

    public BufferedImage drawImagesInGrid(AlbumsResponse response, int limit) {
        albums = new CollageData();
        var gridImage = createGridImage();
        Grid grid = createGrid(limit);
        albums = transformData(response);

        Graphics2D g2d = gridImage.createGraphics();
        setRenderingHints(g2d);
        setFont(g2d);

        int imgWidth = gridImage.getWidth() / grid.getCols();
        int imgHeight = gridImage.getHeight() / grid.getRows();

        List<String> imageUrls = albums.getImageUrls();
        List<String> albumNames = albums.getAlbumNames();
        List<String> artistNames = albums.getArtistNames();

        for (int i = 0; i < grid.getRows(); i++) {
            for (int j = 0; j < grid.getCols(); j++) {
                int index = i * grid.getCols() + j;
                if (index < imageUrls.size()) {
                    ImageDrawingContext context = new ImageDrawingContext(
                            imageUrls.get(index),
                            albumNames.get(index),
                            artistNames.get(index),
                            j * imgWidth,
                            i * imgHeight,
                            imgWidth,
                            imgHeight);
                    drawImageAndText(g2d, context);
                }
            }
        }

        g2d.dispose();
        return gridImage;
    }

    private Grid createGrid(int limit) {
        int size = (int) Math.sqrt(limit);
        return new Grid(size, size);
    }

    public CollageData transformData(AlbumsResponse response) {
        return albums.buildCollageData(response);
    }

    private BufferedImage loadImageFromURL(String urlString) {
        return imageCache.computeIfAbsent(urlString, url -> {
            try {
                URI uri = new URI(url);
                URL urlObj = uri.toURL();
                return CompletableFuture.supplyAsync(() -> {
                    try {
                        return ImageIO.read(urlObj);
                    } catch (IOException e) {
                        throw new ImageCreateException("Error while creating image from URL: " + url);
                    }
                }).join();
            } catch (MalformedURLException | URISyntaxException e) {
                throw new ImageCreateException("Error while creating image from URL: " + urlString);
            }
        });
    }

    private BufferedImage createGridImage() {
        return new BufferedImage(GRID_WIDTH, GRID_HEIGHT, BufferedImage.TYPE_INT_ARGB);
    }

    private void setRenderingHints(Graphics2D g2d) {
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    }

    private void setFont(Graphics2D g2d) {
        Font font = new Font("Arial", Font.PLAIN, 12);
        g2d.setFont(font);
    }

    private void drawImageAndText(Graphics2D g2d, ImageDrawingContext context) {
        try {
            BufferedImage image = loadImageFromURL(context.getImageUrl());
            Image scaledImage = image.getScaledInstance(context.getImgWidth(), context.getImgHeight(),
                    Image.SCALE_SMOOTH);
            g2d.drawImage(scaledImage, context.getX(), context.getY(), context.getImgWidth(), context.getImgHeight(),
                    null);
        } catch (Exception e) {
            drawPlaceholder(g2d, context.getX(), context.getY(), context.getImgWidth(), context.getImgHeight());
        }
        drawText(g2d, context.getAlbumName(), context.getArtistName(), context.getX(), context.getY());
    }

    private void drawPlaceholder(Graphics2D g2d, int x, int y, int width, int height) {
        g2d.setColor(Color.BLACK);
        g2d.fillRect(x, y, width, height);
    }

    private void drawText(Graphics2D g2d, String albumName, String artistName, int x, int y) {
        g2d.setColor(Color.WHITE);
        g2d.drawString(albumName, x + 2, y + 12);
        g2d.drawString(artistName, x + 2, y + 24);
    }
}
