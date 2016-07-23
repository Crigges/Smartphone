package systems.crigges.smartphone;

import java.io.File;
import java.io.FileInputStream;
import java.nio.ByteBuffer;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

public class PCMPlayback {

	private AudioFormat af;
	private DataLine.Info info;
	private SourceDataLine line;
	
	public PCMPlayback() throws LineUnavailableException{
		af = new AudioFormat(22050, 16, 1, true, true);
		info = new DataLine.Info(SourceDataLine.class, af);
		line = (SourceDataLine) AudioSystem.getLine(info);
		line.open(af, 4096);
		line.start();
	}
	
	public void play(byte[] pcmRaw){
		line.write(pcmRaw, 0, pcmRaw.length);
	}

	public static void main(String[] args) {
		try {
			PCMPlayback pcm = new PCMPlayback();
			FileInputStream in = new FileInputStream(new File("pcmaudio"));
			while(true){
				byte[] data = new byte[2048];
				in.read(data);
				pcm.line.write(data, 0, data.length);
			}
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

}
