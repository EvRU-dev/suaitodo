package org.suai.todo.utils;

import java.io.PrintWriter;
import java.io.StringWriter;

import java.util.Locale;
import java.util.Arrays;

public class PrintWriterCount extends PrintWriter {
	public Long len = 0L;
    private PrintWriter printWriter;

    public PrintWriterCount(PrintWriter printWriter) {
    	super(new StringWriter());
        this.printWriter = printWriter;
    }

    @Override
    public PrintWriter append(char c) {
    	throw new UnsupportedOperationException("Forbidden API: append(char c)");
    }

    @Override
    public PrintWriter append(CharSequence csq) {
    	throw new UnsupportedOperationException("Forbidden API: append(CharSequence csq)");
    }

    @Override
    public PrintWriter append(CharSequence csq, int start, int end) {
    	throw new UnsupportedOperationException("Forbidden API: append(CharSequence csq, int start, int end)");
    }

    @Override
    public boolean checkError() {
        return printWriter.checkError();
    }

    @Override
    protected void clearError() {
        throw new UnsupportedOperationException("Forbidden API: clearError()");
    }

    @Override
    public void close() {
        printWriter.close();
    }

    @Override
    public void flush() {
        printWriter.flush();
    }

    @Override
    public PrintWriter format(Locale l, String format, Object... args) {
    	throw new UnsupportedOperationException("Forbidden API: format(Locale l, String format, Object... args)");
    }

    @Override
    public PrintWriter format(String format, Object... args) {
        throw new UnsupportedOperationException("Forbidden API: format(String format, Object... args)");
    }

    @Override
    public void print(boolean b) {
    	print("" + b);
    }

    @Override
    public void print(char c) {
    	print(Character.toString(c));
    }

    @Override
    public void print(char[] s) {
    	print(String.valueOf(s));
    }

    @Override
    public void print(double d) {
    	print("" + d);
    }

    @Override
    public void print(float f) {
    	print("" + f);
    }

    @Override
    public void print(int i) {
    	print("" + i);
    }

    @Override
    public void print(long l) {
        print("" + l);
    }

    @Override
    public void print(Object obj) {
    	print(obj.toString());
    }

    @Override
    public void print(String s) {
        len += strLen(s);
        printWriter.print(s);
    }

    @Override
    public PrintWriter printf(Locale l, String format, Object... args) {
        throw new UnsupportedOperationException("Forbidden API: printf(Locale l, String format, Object... args)");
    }

    @Override
    public PrintWriter printf(String format, Object... args) {
        throw new UnsupportedOperationException("Forbidden API: printf(String format, Object... args)");
    }

    @Override
    public void println() {
    	print(System.lineSeparator());
    }

    @Override
    public void println(boolean x) {
    	print(x + System.lineSeparator());
    }

    @Override
    public void println(char x) {
        print(Character.toString(x) + System.lineSeparator());
    }

    @Override
    public void println(char[] x) {
    	print(String.valueOf(x) + System.lineSeparator());
    }

    @Override
    public void println(double x) {
    	print(x + System.lineSeparator());
    }

    @Override
    public void println(float x) {
    	print(x + System.lineSeparator());
    }

    @Override
    public void println(int x) {
    	print(x + System.lineSeparator());
    }

    @Override
    public void println(long x) {
    	print(x + System.lineSeparator());
    }

    @Override
    public void println(Object x) {
    	print(x.toString() + System.lineSeparator());
    }

    @Override
    public void println(String x) {
    	print(x + System.lineSeparator());
    }

    @Override
    protected void setError() {
        throw new UnsupportedOperationException("Forbidden API: setError()");
    }

    @Override
    public void write(char[] buf) {
    	write(String.valueOf(buf));
    }

    @Override
    public void write(char[] buf, int off, int len) {
    	char[] a = Arrays.copyOfRange(buf, off, off + len);
    	write(a);
    }

    @Override
    public void write(int c) {
    	write("" + c);
    }

    @Override
    public void write(String s) {
    	len += strLen(s);
        printWriter.write(s);
    }

    @Override
    public void write(String s, int off, int len) {
    	this.len += strLen(s.substring(off, len));
        printWriter.write(s, off, len);
    }
    
    private Integer strLen(String s) {
    	try {
    		return s.getBytes("UTF-8").length;
    	} catch (Exception e) {}
    	
    	return 0;
    }
}


