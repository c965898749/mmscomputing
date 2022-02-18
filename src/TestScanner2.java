import ij.IJ;
import ij.ImagePlus;
import uk.co.mmscomputing.device.scanner.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Date;

public class TestScanner2 {

    public static void main(String[] args) {
        Scanner scanner = Scanner.getDevice();
        scanner.addListener(new ScannerListener(){
            @Override
            public void update(ScannerIOMetadata.Type type, ScannerIOMetadata metadata) {
                if (type.equals(ScannerIOMetadata.ACQUIRED)) {

                    BufferedImage image=metadata.getImage();
                    System.out.println("Have an image now!");
                    try{
                        ImageIO.write(image, "png", new File("D:\\mmsc_image.png"));
                    }catch(Exception e){
                        e.printStackTrace();
                    }

                } else if (type.equals(ScannerIOMetadata.NEGOTIATE)) {
                    ScannerDevice device = metadata.getDevice();
                    try {
                        device.setResolution(100);
                    } catch (ScannerIOException e) {
                        IJ.error(e.getMessage());
                    }
                    /*
                     * More options if necessary! try{
                     * device.setShowUserInterface(true);
                     * device.setShowProgressBar(true);
                     * device.setRegionOfInterest(0,0,210.0,300.0);
                     * device.setResolution(100); }catch(Exception e){
                     * e.printStackTrace(); }
                     */
                } else if (type.equals(ScannerIOMetadata.STATECHANGE)) {

                    IJ.error(metadata.getStateStr());
                } else if (type.equals(ScannerIOMetadata.EXCEPTION)) {
                    IJ.error(metadata.getException().toString());

                }
            }
        });
        try {
            String [] scannerName = scanner.getDeviceNames();
            while (scanner.isBusy()){

            }
            scanner.select(scannerName[0]);
            Thread.sleep(5000);
            scanner.acquire();
            while (true){

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
