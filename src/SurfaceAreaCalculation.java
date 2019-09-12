/**
 * This JAVA file computes the surface area of the placenta using the voxel coordinates read in
 * from its text file. The algorithm determines which voxels do not have neighbors on any one of
 * their sides (left, right, top, bottom, front, or back) and stores them into a separate hash map.
 * Then, the surface area of all the voxels is computed using the surface area formula of a
 * rectangular prism (Voxels are rectangular prisms).
 *
 *              Output:
 *                      area: Surface area of the placenta
 *
 * Written by Archana Dhyani 8/31/2019
 * University of Wisconsin-Madison
 *
 */
import java.io.File;
import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class SurfaceAreaCalculation {

  private HashMap<Integer, Float> p_rl;
  private HashMap<Integer, Float> p_fh;
  private HashMap<Integer, Float> p_ap;

  /**
   * This public constructor instantiates the above hash maps that would store the placenta voxel
   * coordinates from its respective text file.
   */
  public SurfaceAreaCalculation() {
    this.p_rl = new HashMap<>();
    this.p_fh = new HashMap<>();
    this.p_ap = new HashMap<>();
  }


  /**
   * This method reads in the text file that contains the placental voxel coordinates. It then
   * stores these coordinates into hash maps.
   * @param file is the text file that contains the placental voxel coordinates
   * @throws FileNotFoundException is an exception thrown in case if a file does not exist
   */
  public void readInPlacentalSegmentation(String file) throws FileNotFoundException {
    Scanner reader = new Scanner(new File(file));
    String data;
    int i = 0;
    while (reader.hasNextLine()) {
      data = reader.nextLine();
      String[] values = data.split(", ");
      p_rl.put(i, Float.parseFloat(values[0]));
      p_fh.put(i, Float.parseFloat(values[1]));
      p_ap.put(i, Float.parseFloat(values[2]));
      i++;
    }
    reader.close();
  }

  /**
   * This method mimics a circular shift of the array elements instead of including an additional
   * for-loop.
   * @param arr is the array whose elements get shifted by one
   * @return the first element of the array which is a float
   */
  private float rotate(float[] arr)
  {
    float x = arr[arr.length-1];
    for (int i= arr.length-1; i > 0; i--) {
      arr[i] = arr[i-1];
    }
    arr[0] = x;
    return arr[0];
  }


  /**
   * This method calculates the surface area of the placenta. It checks which voxels do not have
   * neighbors on any one of their sides, stores them into a separate hash map and then computes
   * the surface area of those voxels.
   * @return area which is in the form of a String due to decimal formatting.
   */
  public String calculateSurfaceArea() {
    float[] parray_rl = new float[p_rl.size()]; //As size differs per case, all the elements
    //within the hash maps are stored into arrays
    float[] parray_fh = new float[p_fh.size()];
    float[] parray_ap = new float[p_ap.size()];
    float x=0;
    float y=0;
    float z=0;
    float area=0;
    ArrayList<Float> set_rl = new ArrayList<>();
    ArrayList<Float> set_fh = new ArrayList<>();
    ArrayList<Float> set_ap = new ArrayList<>();
    for (int i = 0; i < p_rl.size(); i++) {
      parray_rl[i] = p_rl.get(i);
      set_rl.add(parray_rl[i]);
      parray_fh[i] = p_fh.get(i);
      set_fh.add(parray_fh[i]);
      parray_ap[i] = p_ap.get(i);
      set_ap.add(parray_ap[i]);
    }
    int rl=0;
    int ap=0;
    int fh=0;
    HashMap<Integer, Float> placenta_rl = new HashMap<Integer, Float>();
    HashMap<Integer, Float> placenta_fh = new HashMap<Integer, Float>();
    HashMap<Integer, Float> placenta_ap = new HashMap<Integer, Float>();
    float voxel_rl =0;
    float voxel_fh=0;
    float voxel_ap=0;
    for(int i=0; i<p_rl.size(); i++) {
      voxel_rl = rotate(parray_rl);
      voxel_fh = rotate(parray_fh);
      voxel_ap = rotate(parray_ap);
      if(!set_rl.contains(p_rl.get(i)-1) || !set_rl.contains(p_rl.get(i)+1)) {
        placenta_rl.put(rl, p_rl.get(i));
        rl++;
      }
      if(!set_fh.contains(p_fh.get(i)-1) || !set_fh.contains(p_fh.get(i)+1)) {
        placenta_fh.put(fh, p_fh.get(i));
        fh++;
      }
      if(!set_ap.contains(p_ap.get(i)-1) || !set_ap.contains(p_ap.get(i)+1)) {
        placenta_ap.put(ap, p_ap.get(i));
        ap++;
      }
    }



    for(int i=0; i<placenta_rl.size(); i++) {
      x = Math.abs(placenta_rl.get(i));
      y = Math.abs(placenta_fh.get(i));
      z = Math.abs(placenta_ap.get(i));
      area+=2*(x*y + y*z + x*z);
    }
    DecimalFormat df = new DecimalFormat("0.0");
    return df.format(area);


  }



  public static void main(String[] args) {
    SurfaceAreaCalculation placenta = new SurfaceAreaCalculation();
    try {
      placenta.readInPlacentalSegmentation("placenta.txt");
      System.out.print(placenta.calculateSurfaceArea());
    } catch (FileNotFoundException e) {
      System.out.println("File not found");
      e.printStackTrace();
    }

  }
}
