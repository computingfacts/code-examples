package com.computingfacts.snippets.fileOperation;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.DirectoryIteratorException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import static java.util.Collections.singletonList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author joseph
 */
@Slf4j
public final class FileUtil {

    private static final String DEFAULT_PERMISSION = "rwxr-xr-x";

    private FileUtil() {

    }

    public static void createDirectoriesWithPermission(Path directories, String permission) throws IOException {
        if (Objects.isNull(permission)) {
            permission = DEFAULT_PERMISSION;
        }
        FileAttribute<Set<PosixFilePermission>> attr = fileAttributes(permission);
        Files.createDirectories(directories, attr);
    }

    //Filtering a Directory Listing By Using Globbing
    public static List<Path> listFilesByExtension(Path dir, String filename) {
        //filename can be any file extension e.g "*.{java,class,json,log,txt}"
        try ( DirectoryStream<Path> dirStream = Files.newDirectoryStream(dir, filename)) {

            return StreamSupport
                    .stream(dirStream.spliterator(), false)
                    .collect(Collectors.toList());

        } catch (IOException | DirectoryIteratorException x) {
            log.error(x.getMessage());
        }
        return List.of();
    }

    public static List<Path> listFiles(Path pathToDir) {
        try ( Stream<Path> files = Files.list(pathToDir)) {
            return files
                    .filter(Files::isRegularFile)
                    .collect(Collectors.toList());
        } catch (IOException ex) {
            log.error(ex.getMessage());
        }
        return List.of();
    }

    public static List<Path> listFilesAndDirs(Path dir) {
        try ( Stream<Path> files = Files.walk(dir)) {
            return files
                    .collect(Collectors.toList());

        } catch (IOException ex) {
            log.error(ex.getMessage());
        }
        return List.of();
    }

    public static File createRegularFile(Path path) throws IOException {

        if (Files.notExists(path, LinkOption.NOFOLLOW_LINKS)) {
            return Files.createFile(path, fileAttributes(DEFAULT_PERMISSION)).toFile();
        }
        return path.toFile();

    }

    public static void writeTextToFile(Path path, String text) throws IOException {
        Files.write(path, singletonList(text), StandardOpenOption.CREATE);
    }

    public static List<String> readFromFile(Path path) throws IOException {
        return Files.readAllLines(path);
    }

    public static void moveFile(Path source, Path destination) throws IOException {
        Files.move(source, destination,
                StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);

    }

    public static void downloadWebPage(@NonNull String webPage, Path destination) throws IOException {

        URI uri = URI.create(webPage);
        try ( InputStream in = uri.toURL().openStream()) {
            Files.copy(in, destination, StandardCopyOption.REPLACE_EXISTING);
        }

    }

    public static boolean deleteFile(Path path) throws IOException {

        return Files.deleteIfExists(path);

    }

    public static void deleteFilesAndLinks(Path path) throws IOException {

        Files.walk(path, FileVisitOption.FOLLOW_LINKS)
                .map(Path::toFile)
                .forEach(File::delete);

    }

    private static FileAttribute<Set<PosixFilePermission>> fileAttributes(@NonNull String permission) {
        Set<PosixFilePermission> perms = PosixFilePermissions.fromString(permission);

        return PosixFilePermissions.asFileAttribute(perms);
    }

}
