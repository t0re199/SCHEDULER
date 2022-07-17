package documents;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import org.json.JSONObject;

public class JSONExportableDocument implements ExportableDocument
{
	private JSONObject jsonObject;
	
	public JSONExportableDocument(JSONObject jsonObject)
	{
		if(jsonObject == null)
		{
			throw new IllegalArgumentException();
		}
		this.jsonObject = jsonObject;
	}

	@Override
	public void export(File file) throws IOException
	{
		if(file == null)
		{
			throw new IllegalArgumentException("A directory was expected!");
		}
		PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file)));
		jsonObject.write(pw);
		pw.close();
	}
}
