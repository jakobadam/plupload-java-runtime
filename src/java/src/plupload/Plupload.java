package plupload;

import java.io.File;
import java.io.IOException;
import java.security.AccessControlException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFileChooser;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;

import netscape.javascript.JSObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.SystemLog;

/**
 * Plupload Java runtime.
 *
 * This class is optimized by proguard. Unused members are striped by
 * proguard.
 *
 * Therefore members to be used from JS should be specified in build.xml
 */
public class Plupload extends Applet2 {
	
	private static Log log = LogFactory.getLog(Plupload.class);
	
	private PluploadFile current_file;
	private JFileChooser dialog;
	private boolean dialog_open = false;

	private String uploader_id;
	private Map<String, PluploadFile> files;
	private int id_counter = 0;
	
	@Override
	public void init() {
		super.init();
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}

		SystemLog.currentLogLevel = Integer.parseInt(getParameter("log_level", /*LOG_LEVEL_ERROR*/"5"));
		
		files = new HashMap<String, PluploadFile>();
		uploader_id = getParameter("id");

		try {
			dialog = new JFileChooser();
			dialog.setMultiSelectionEnabled(true);
		} catch (AccessControlException e) {
			JSObject
					.getWindow(this)
					.eval(
							"alert('Please approve the digital signature of the applet. Close the browser and start over')");
		}
		publishEvent(Event.INIT);
	}

	@SuppressWarnings("unchecked")
	public void setFileFilter(final String description, final String[] filters) {
		log.debug("setFileFilter: " + description + " " + Arrays.toString(filters));
		try {
			AccessController.doPrivileged(new PrivilegedExceptionAction() {
				public Object run() throws IOException, Exception {
					FileNameExtensionFilter filter = new FileNameExtensionFilter(description, filters);
					dialog.setFileFilter(filter);
					return null;
				}
			});
		} catch (PrivilegedActionException e) {
			Exception ex = e.getException();
			if (ex instanceof IOException) {
				publishIOError(ex);
			} else if (ex instanceof Exception) {
				publishError(ex);
			}
		}

	}

	// LiveConnect calls from JS
	@SuppressWarnings("unchecked")
	public void uploadFile(final String id, final String url,
			final String cookie, final int chunk_size, final int retries) {
		log.debug("uploadFile: id: " + id + " url: " + url + " cookie: " + cookie + " chunk_size: " + chunk_size + " retries: " + retries);
		final PluploadFile file = files.get(id);
		if (file != null) {
			this.current_file = file;
		}

		try {
			// Because of LiveConnect our security privileges are degraded
			// elevate them again.
			AccessController.doPrivileged(new PrivilegedExceptionAction() {
				public Object run() throws IOException, Exception {
					file.upload(url, chunk_size, retries, cookie);
					return null;
				}
			});
		} catch (PrivilegedActionException e) {
			Exception ex = e.getException();
			if (ex instanceof IOException) {
				publishIOError(ex);
			} else {
				publishError(ex);
			}
		}
	}

	public void removeFile(String id) {
		files.remove(id);
	}

	public void clearFiles() {
		files.clear();
	}

	@SuppressWarnings("unchecked")
	public void openFileDialog() {
		log.debug("opening dialog");
		if (dialog_open) {
			// FIXME: bring openDialog to front
			return;
		}
		dialog_open = true;
		AccessController.doPrivileged(new PrivilegedAction() {
			public Object run() {
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						int file_chose_return_value = dialog
								.showOpenDialog(Plupload.this);
						log.debug("openDialog finished");
						// blocks until file selected
						if (file_chose_return_value == JFileChooser.APPROVE_OPTION) {
							for (File f : dialog.getSelectedFiles()) {
								// Wiredness: If PluploadFile extends Thread
								// it just stopped here in my production
								// environment
								PluploadFile file = new PluploadFile(
										id_counter++, f);
								selectEvent(file);
							}
						}
						dialog_open = false;
					}
				});
				return null;
			}
		});
	}

	// Events
	private enum Event {
		CLICK("Click"), 
		INIT("Init"),
		SELECT_FILE("SelectFiles"), 
		UPLOAD_PROCESS("UploadProcess"), 
		UPLOAD_CHUNK_COMPLETE("UploadChunkComplete"), 
		SKIP_UPLOAD_CHUNK_COMPLETE("SkipUploadChunkComplete"), 
		IO_ERROR("IOError"), 
		GENERIC_ERROR("GenericError");

		private String name;

		Event(String name) {
			this.name = name;
		}

		private String getName() {
			return name;
		}
	}

	private void publishEvent(Event e, Object ... args) {
		// is this really the way to do this?
		// prepend args with upload id.
		Object[] new_args = new Object[args.length + 1];
		new_args[0] = uploader_id;
		System.arraycopy(args, 0, new_args, 1, args.length);
		// calls to superclass
		publishEvent(e.getName(), new_args);
	}

	private void publishIOError(Exception e) {
		publishEvent(Event.IO_ERROR, new PluploadError(e.getMessage(), this.current_file.id));
	}

	private void publishError(Exception e) {
		publishEvent(Event.GENERIC_ERROR, new PluploadError(e.getMessage(), this.current_file.id));
	}

	private void selectEvent(PluploadFile file) {
		log.debug("selectEvent");
		// handles file add from file chooser
		files.put(file.id + "", file);

		file.addFileUploadListener(new FileUploadListener() {

			@Override
			public void uploadProcess(PluploadFile file) {
				publishEvent(Event.UPLOAD_PROCESS, file);
			}

			@Override
			public void ioError(IOException e) {
				publishIOError(e);

			}

			@Override
			public void genericError(Exception e) {
				publishError(e);
			}
		});

		publishEvent(Event.SELECT_FILE, file.toString());
	}
}
