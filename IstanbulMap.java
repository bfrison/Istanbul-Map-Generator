package istanbulmapgenerator;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class IstanbulMap{

    private final List<Integer> tiles;
    private static final int FOUNTAIN = 7;
    private static final int BLACK_MARKET = 8;
    private static final int TEA_HOUSE = 9;
    private final int mapSize;
    private final int numberColumns;
    private final List<Integer> acceptableFountainPositions;

    public IstanbulMap(GameVersion gameVersion, Random r) {
        switch (gameVersion) {
            case ORIGINAL:
                mapSize = 16;
                numberColumns = 4;
                acceptableFountainPositions = Arrays.asList(5, 6, 9, 10);
                break;
            case MOCHA_BAKSHEESH:
                mapSize = 20;
                numberColumns = 5;
                acceptableFountainPositions = Arrays.asList(6, 7, 8, 11, 12, 13);
                break;
            default:
                throw new UnsupportedOperationException("Game Version is not supported");
        }

        this.tiles = new ArrayList<>(tilesGenerator(r));
        enforceFountainRule(r);
        enforceBlackMarketTeaHouseRule(r);

    }

    public IstanbulMap(GameVersion gameVersion) {
        this(gameVersion, new Random());
    }
    
    private void enforceBlackMarketTeaHouseRule(Random r) {
        int indexBlackMarket = this.tiles.indexOf(BLACK_MARKET);
        int indexTeaHouse = this.tiles.indexOf(TEA_HOUSE);
        while (getDistance(indexBlackMarket, indexTeaHouse) < 3) {
            int newIndex = r.nextInt(mapSize);
            if (this.tiles.get(newIndex) != FOUNTAIN && this.tiles.get(newIndex) != TEA_HOUSE) {
                Collections.swap(this.tiles, indexBlackMarket, newIndex);
            }
            indexBlackMarket = this.tiles.indexOf(BLACK_MARKET);
            indexTeaHouse = this.tiles.indexOf(TEA_HOUSE);
        }
    }

    private void enforceFountainRule(Random r) {
        int indexFountain = this.tiles.indexOf(FOUNTAIN);
        if (!acceptableFountainPositions.contains(indexFountain)) {
            int newIndexFountain = r.nextInt(acceptableFountainPositions.size());
            newIndexFountain = acceptableFountainPositions.get(newIndexFountain);
            Collections.swap(this.tiles, indexFountain, newIndexFountain);
        }
    }

    public List<Integer> tilesGenerator(Random r) {
        return r.ints(1, mapSize + 1)
                .distinct()
                .limit(mapSize)
                .boxed()
                .collect(Collectors.toList());
    }

    public int getXCoordinate(int index) {
        return index % numberColumns + 1;
    }

    public int getYCoordinate(int index) {
        return index / numberColumns + 1;
    }

    public int getIndexFromCoordinate(int x, int y) {
        return numberColumns * (y - 1) + x - 1;
    }

    public int getDistance(int firstIndex, int secondIndex) {
        int deltaX = getXCoordinate(secondIndex) - getXCoordinate(firstIndex);
        int deltaY = getYCoordinate(secondIndex) - getYCoordinate(firstIndex);
        return Math.abs(deltaX) + Math.abs(deltaY);
    }

    //This method is used to tiles list:
    public void printMap(PrintStream printStream) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < mapSize; i++) {
            stringBuilder.append(String.format("%02d", this.tiles.get(i)));
            if ((i + 1) % numberColumns == 0) {
                stringBuilder.append("\n");
            } else {
                stringBuilder.append(" ");
            }
        }
        printStream.println(stringBuilder.toString());
    }

    public List<Integer> getTiles() {
        return tiles;
    }

    public int getMapSize() {
        return mapSize;
    }

    public enum GameVersion {
        ORIGINAL, MOCHA_BAKSHEESH;
    }
}
