/**
 * 
 */
package com.csulb.cecs529.set.homeworks;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * @author Manav
 *
 */
public class MyFileVisitor implements FileVisitor<Path> {

	Path directoryPath;

	public MyFileVisitor() {
		// TODO Auto-generated constructor stub
	}

	public MyFileVisitor(Path directoryPath) {
		super();
		this.directoryPath = directoryPath;
	}

	@Override
	public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
		// make sure we only process the current working directory
		if (this.directoryPath.equals(dir)) {
			return FileVisitResult.CONTINUE;
		}

		// Continue without visiting the entries in this directory.
		// This result is only meaningful when returned from the
		// preVisitDirectory method;
		// otherwise this result type is the same as returning CONTINUE.

		// When we are traversing the directory pointed to by this.directory
		// method, we don't want to visit the files inside any of the
		// subdirectories and so we return the SKIP_SUBTREE
		return FileVisitResult.SKIP_SUBTREE;
	}

	@Override
	public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

}
