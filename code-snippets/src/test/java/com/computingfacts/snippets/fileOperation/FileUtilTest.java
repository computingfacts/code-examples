package com.computingfacts.snippets.fileOperation;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import static java.util.Collections.singletonList;
import java.util.List;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 *
 * @author joseph
 */
public class FileUtilTest {

    /**
     * Test of all methods, of class FileUtil.
     *
     * @param tempDir
     * @throws java.io.IOException
     */
    @Test
    public void testFileUtilMethods(@TempDir Path tempDir) throws IOException {

        Path dev = tempDir.resolve("dev");
        Path prod = tempDir.resolve("prod");
        Path subDir = dev.resolve("subDir");

        //create dir
        FileUtil.createDirectoriesWithPermission(dev, "rwxr--r--");
        FileUtil.createDirectoriesWithPermission(subDir, null);

        assertAll(
                () -> assertTrue(Files.isDirectory(dev)),
                () -> assertTrue(Files.isDirectory(subDir)),
                () -> assertTrue(Files.notExists(prod)));

        //create regular files
        File readmeFile = FileUtil.createRegularFile(dev.resolve("README.md"));
        File logFile = FileUtil.createRegularFile(dev.resolve("sys.log"));
        File subDirFile = FileUtil.createRegularFile(subDir.resolve("subDir.log"));

        assertAll(
                () -> assertTrue(Files.isRegularFile(readmeFile.toPath())),
                () -> assertTrue(Files.isRegularFile(subDirFile.toPath())),
                () -> assertTrue(Files.isRegularFile(logFile.toPath())));

        //write to file
        String text = "This is a readme file";
        FileUtil.writeTextToFile(dev.resolve("README.md"), text);

        assertLinesMatch(singletonList("This is a readme file"),
                Files.readAllLines(dev.resolve("README.md")));
        assertLinesMatch(singletonList("This is a readme file"),
                FileUtil.readFromFile(dev.resolve("README.md")));

        //copy files
        FileUtil.downloadWebPage("https://www.computingfacts.com/",
                dev.resolve("web.html"));

        assertTrue(Files.isRegularFile(dev.resolve("web.html")));

        //list files by file extension
        List<Path> filesByExtension = FileUtil.listFilesByExtension(dev, "*.{md,html}");

        assertThat(filesByExtension, hasSize(2));

        //list all files
        List<Path> dirs = FileUtil.listFiles(dev);

        assertThat(dirs, hasSize(3));

        List<Path> subDirs = FileUtil.listFilesAndDirs(dev);

        assertThat(subDirs, hasSize(6));
        assertThat(subDirs, hasItem(subDir));

        assertTrue(subDirs.size() > dirs.size());

        //move files and dir
        FileUtil.moveFile(dev, prod);
        assertAll(
                () -> assertTrue(Files.isDirectory(prod)),
                () -> assertTrue(Files.notExists(dev)));

        assertLinesMatch(singletonList("This is a readme file"),
                Files.readAllLines(prod.resolve("README.md")));

        //delete files and dir
        FileUtil.deleteFilesAndLinks(tempDir);

        //validate delete
        List<Path> emptyDir = FileUtil.listFiles(tempDir);
        assertThat(emptyDir, hasSize(0));
    }

}
