package at.neonartworks.jgagv2.core.listener;

import java.io.IOException;
import java.io.OutputStream;

import org.apache.http.HttpEntity;
import org.apache.http.entity.HttpEntityWrapper;

public class ProgressEntityWrapper extends HttpEntityWrapper
{

	private ProgressListener listener;

	public ProgressEntityWrapper(HttpEntity entity, ProgressListener listener)
	{
		super(entity);
		this.listener = listener;
	}

	@Override
	public void writeTo(OutputStream outstream) throws IOException
	{
		super.writeTo(new CountingOutputStream(outstream, this.listener, getContentLength()));
	}
}