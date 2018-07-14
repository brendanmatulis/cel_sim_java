package prototype.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Scanner;

public class GeneralUtils {
    public static String loadAsString(String file) {
        StringBuilder result = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(
                            GeneralUtils.class.getResourceAsStream(file)
                    ));

            String buffer;
            while ((buffer = reader.readLine()) != null) {
                result.append(buffer + '\n');
            }
            reader.close();
            return result.toString();
        } catch (IOException | NullPointerException e) {
            System.err.println("Error loading file \"" + file + "\"");
            e.printStackTrace();
        }
        return null;
    }

    public static ArrayList<String> readAllLines(String fileName) {
        ArrayList<String> list = new ArrayList<>();

        InputStream is = OBJLoader.class.getResourceAsStream(fileName);
        Scanner scanner = new Scanner(is);
        while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                list.add(line);
        }

        return list;
    }

    public static ByteBuffer createBuffer(byte[] array) {
        ByteBuffer result = ByteBuffer
                .allocateDirect(array.length)
                .order(ByteOrder.nativeOrder());
        result.put(array).flip();
        return result;
    }

    public static FloatBuffer createBuffer(float[] array) {
        FloatBuffer result = ByteBuffer
                .allocateDirect(array.length << 2)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        result.put(array).flip();
        return result;
    }

    public static IntBuffer createBuffer(int[] array) {
        IntBuffer result = ByteBuffer.allocateDirect(array.length << 2)
                .order(ByteOrder.nativeOrder())
                .asIntBuffer();
        result.put(array).flip();
        return result;
    }

    public static float[] floatListToArray(ArrayList<Float> floatList) {
        float[] result = new float[floatList.size()];

        for (int i = 0; i < floatList.size(); i++) {
            result[i] = floatList.get(i);
        }
        return result;
    }
}
