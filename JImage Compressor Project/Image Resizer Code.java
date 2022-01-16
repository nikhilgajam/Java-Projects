import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;

public class Jp{

    public static void main(String[] args){

        resize("C:\\Users\\DELL\\Downloads\\img.jpeg", "C:\\Users\\DELL\\Downloads\\resized.jpeg", 1080, 1080);

    }

    public static void resize(String in_img_path, String out_img_path, int compression_width, int compression_height){

        try{

            // Reading input image
            File in_path = new File(in_img_path);
            BufferedImage in_img = ImageIO.read(in_path);

            // Output image
            BufferedImage out_img = new BufferedImage(compression_width, compression_height, in_img.getType());

            // Resizing the image
            Graphics2D graphics = out_img.createGraphics();
            graphics.drawImage(in_img, 0, 0, compression_width, compression_height, null);
            graphics.dispose();

            // Extension extraction
            String ext = out_img_path.substring(out_img_path.lastIndexOf(".")+1);

            // Writing the image to disk
            ImageIO.write(out_img, ext, new File(out_img_path));

            System.out.println("Resized Successfully");

        }catch(Exception e){
            e.printStackTrace();
        }

    }

}
