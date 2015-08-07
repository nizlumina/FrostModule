package org.apache.commons.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Origin of code: Excalibur, Alexandria, Commons-Utils
 * <p/>
 * Extracted methods from Commons IO
 */
public class FileUtils
{
    /**
     * Opens a {@link FileOutputStream} for the specified file, checking and
     * creating the parent directory if it does not exist.
     * <p/>
     * At the end of the method either the stream will be successfully opened,
     * or an exception will have been thrown.
     * <p/>
     * The parent directory will be created if it does not exist.
     * The file will be created if it does not exist.
     * An exception is thrown if the file object exists but is a directory.
     * An exception is thrown if the file exists but cannot be written to.
     * An exception is thrown if the parent directory cannot be created.
     *
     * @param file   the file to open for output, must not be {@code null}
     * @param append if {@code true}, then bytes will be added to the
     *               end of the file rather than overwriting
     * @return a new {@link FileOutputStream} for the specified file
     * @throws IOException if the file object is a directory
     * @throws IOException if the file cannot be written to
     * @throws IOException if a parent directory needs creating but that fails
     * @since 2.1
     */
    public static FileOutputStream openOutputStream(File file, boolean append) throws IOException
    {
        if (file.exists())
        {
            if (file.isDirectory())
            {
                throw new IOException("File '" + file + "' exists but is a directory");
            }
            if (file.canWrite() == false)
            {
                throw new IOException("File '" + file + "' cannot be written to");
            }
        }
        else
        {
            File parent = file.getParentFile();
            if (parent != null)
            {
                if (!parent.mkdirs() && !parent.isDirectory())
                {
                    throw new IOException("Directory '" + parent + "' could not be created");
                }
            }
        }
        return new FileOutputStream(file, append);
    }

    /**
     * Writes a byte array to a file creating the file if it does not exist.
     * <p/>
     * NOTE: As from v1.3, the parent directories of the file will be created
     * if they do not exist.
     *
     * @param file the file to write to
     * @param data the content to write to the file
     * @throws IOException in case of an I/O error
     * @since 1.1
     */
    public static void writeByteArrayToFile(File file, byte[] data) throws IOException
    {
        writeByteArrayToFile(file, data, false);
    }

    /**
     * Writes a byte array to a file creating the file if it does not exist.
     *
     * @param file   the file to write to
     * @param data   the content to write to the file
     * @param append if {@code true}, then bytes will be added to the
     *               end of the file rather than overwriting
     * @throws IOException in case of an I/O error
     * @since IO 2.1
     */
    public static void writeByteArrayToFile(File file, byte[] data, boolean append) throws IOException
    {
        OutputStream out = null;
        try
        {
            out = openOutputStream(file, append);
            out.write(data);
            out.close(); // don't swallow close Exception if copy completes normally
        }
        finally
        {
            IOUtils.closeQuietly(out);
        }
    }

    /**
     * Reads the contents of a file into a byte array.
     * The file is always closed.
     *
     * @param file the file to read, must not be {@code null}
     * @return the file contents, never {@code null}
     * @throws IOException in case of an I/O error
     * @since 1.1
     */
    public static byte[] readFileToByteArray(File file) throws IOException
    {
        InputStream in = null;
        try
        {
            in = openInputStream(file);
            return IOUtils.toByteArray(in, file.length());
        }
        finally
        {
            IOUtils.closeQuietly(in);
        }
    }

    //-----------------------------------------------------------------------

    /**
     * Opens a {@link FileInputStream} for the specified file, providing better
     * error messages than simply calling <code>new FileInputStream(file)</code>.
     * <p/>
     * At the end of the method either the stream will be successfully opened,
     * or an exception will have been thrown.
     * <p/>
     * An exception is thrown if the file does not exist.
     * An exception is thrown if the file object exists but is a directory.
     * An exception is thrown if the file exists but cannot be read.
     *
     * @param file the file to open for input, must not be {@code null}
     * @return a new {@link FileInputStream} for the specified file
     * @throws FileNotFoundException if the file does not exist
     * @throws IOException           if the file object is a directory
     * @throws IOException           if the file cannot be read
     * @since 1.3
     */
    public static FileInputStream openInputStream(File file) throws IOException
    {
        if (file.exists())
        {
            if (file.isDirectory())
            {
                throw new IOException("File '" + file + "' exists but is a directory");
            }
            if (file.canRead() == false)
            {
                throw new IOException("File '" + file + "' cannot be read");
            }
        }
        else
        {
            throw new FileNotFoundException("File '" + file + "' does not exist");
        }
        return new FileInputStream(file);
    }

}
