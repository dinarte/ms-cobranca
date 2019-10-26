package br.com.dfframeworck.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PackageUtils {
	public static List<Class<?>> getClassList(String pckgname) throws ClassNotFoundException {
		List<Class<?>> classes = new ArrayList<Class<?>>();

		File directory = null;
		try {
			String directoryName = Thread.currentThread().getContextClassLoader()
					.getResource(pckgname.replace('.', '/')).getFile().replaceAll("%20", " ");
			directory = new File(directoryName);
		} catch (NullPointerException x) {
			throw new ClassNotFoundException(pckgname + " does not appear to be a valid package1");
		}

		if (directory.exists()) {
			String[] files = directory.list();
			for (int i = 0; i < files.length; i++) {
				if (!files[i].endsWith(".class")) {
					continue;
				}
				classes.add(Class.forName(pckgname + '.' + files[i].substring(0, files[i].length() - 6)));
			}

			if (classes.size() < 1) {
				throw new ClassNotFoundException(pckgname + " don't have class into the package informad");
			}
		} else {
			throw new ClassNotFoundException(pckgname + " does not appear to be a valid package2");
		}
		return classes;
	}

	public static void main(String[] args) throws ClassNotFoundException {
		List<Class<?>> classes = getClassList("br.com.memes.metaoptata.domain");
		for (Class<?> clazz : classes) {
			System.out.println(clazz.getName());
		}
	}
}
