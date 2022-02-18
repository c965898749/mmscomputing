import ij.IJ;
import ij.ImagePlus;
import ij.plugin.PlugIn;
import uk.co.mmscomputing.device.scanner.*;
import uk.co.mmscomputing.device.twain.TwainIdentity;
import uk.co.mmscomputing.device.twain.TwainScanner;
import uk.co.mmscomputing.device.twain.jtwain;

import java.util.Enumeration;
import java.util.Vector;

public class TestScanner implements PlugIn {
    public static void main(String[] args) {
        try {
        Scanner scanner = new TwainScanner();

        String [] scannerName = scanner.getDeviceNames();

        while (scanner.isBusy()){

        }
        jtwain.select((TwainScanner)scanner,scannerName[1]);
        //scanner.select();
        Thread.currentThread().sleep(5000);
        String name = scanner.getSelectedDeviceName();
        System.out.println(name);
        jtwain.acquire((TwainScanner)scanner);

            scanner.addListener(new ScannerListener(){
                @Override
                public void update(ScannerIOMetadata.Type type, ScannerIOMetadata metadata) {
                    if (type.equals(ScannerIOMetadata.ACQUIRED)) {

                        ImagePlus imp = new ImagePlus("Scan", metadata.getImage());
                        imp.show();
                        metadata.setImage(null);
                        try {
                            new uk.co.mmscomputing.concurrent.Semaphore(0, true).tryAcquire(2000, null);
                        } catch (InterruptedException e) {
                            IJ.error(e.getMessage());
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

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run(String s) {

    }


}
