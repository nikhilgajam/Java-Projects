import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;

public class Jp{

    public static void main(String[] args){

        compress("C:\\Users\\DELL\\Downloads\\img.jpeg", "C:\\Users\\DELL\\Downloads\\compressed.jpeg", 1920);

    }

    public static void compress(String in_img_path, String out_img_path, int compression_width){

        try{

            // Reading input image
            File in_path = new File(in_img_path);
            BufferedImage in_img = ImageIO.read(in_path);

            // Image compression calculation
            float w = (compression_width/(float)in_img.getWidth());
            float h = ((float)in_img.getHeight()*w);
            int height = (int)h;
//            System.out.println(in_img.getWidth() + ", " + in_img.getHeight() + " : " + width + ", " + height);

            // Output image
            BufferedImage out_img = new BufferedImage(compression_width, height, in_img.getType());

            // Resizing the image
            Graphics2D graphics = out_img.createGraphics();
            graphics.drawImage(in_img, 0, 0, compression_width, height, null);
            graphics.dispose();

            // Extension extraction
            String ext = out_img_path.substring(out_img_path.lastIndexOf(".")+1);

            // Writing the image to disk
            ImageIO.write(out_img, ext, new File(out_img_path));

            System.out.println("Compressed Successfully");

        }catch(Exception e){
            e.printStackTrace();
        }

    }

}
