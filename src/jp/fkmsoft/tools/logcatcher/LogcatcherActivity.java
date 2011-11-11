package jp.fkmsoft.tools.logcatcher;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class LogcatcherActivity extends Activity 
	implements OnClickListener {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Button b = (Button)findViewById(R.id.run);
        b.setOnClickListener(this);
        
    }

	@Override
	public void onClick(View v) {
		// file name
		String path = Environment.getExternalStorageDirectory() + 
			File.separator + "Android" +  
			File.separator + "data" + 
			File.separator + "jp.fkmsoft.tools.logcatcher" + 
			File.separator + "files";
		File folder = new File(path);
		if (!folder.exists() || !folder.isDirectory()) {
			folder.mkdirs();
		}
		
		String fileName = path + File.separator + 
				"log_" + System.currentTimeMillis() + ".txt";
		
		Runtime r = Runtime.getRuntime();
		Process p = null;
		InputStream in = null;
		InputStreamReader sr = null;
		BufferedReader br = null;
		
		FileOutputStream out = null;
		OutputStreamWriter ow = null;
		BufferedWriter bw = null;
		try {
			p = r.exec("logcat -d");
			in = p.getInputStream();
			sr = new InputStreamReader(in);
			br = new BufferedReader(sr);
			
			out = new FileOutputStream(fileName);
			ow = new OutputStreamWriter(out);
			bw = new BufferedWriter(ow);
			
			String line;
			while ((line = br.readLine()) != null) {
				bw.write(line);
				bw.write("\n");
			}
			bw.flush();

			Toast.makeText(this, "Saved\n" + fileName, Toast.LENGTH_LONG).show();
			
			Intent it = new Intent(Intent.ACTION_SEND);
			it.setType("text/plain");
			it.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + fileName));
			//it.putExtra(Intent.EXTRA_TEXT, sb.toString());
			startActivity(it);
		} catch (IOException e) {
			Toast.makeText(this, "IO Exception " + e.getMessage(), Toast.LENGTH_LONG).show();
			e.printStackTrace();
		} finally {
			try {
				if (br != null) { br.close(); }
				if (sr != null) { br.close(); }
				if (in != null) { br.close(); }
				
				if (bw != null) { br.close(); }
				if (ow != null) { br.close(); }
				if (out != null) { br.close(); }
			} catch (IOException ie)  {
				ie.printStackTrace();
			}
		}
		
	}
}