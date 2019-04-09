package kr.hs.dgsw.springcrmpractice.service;

import kr.hs.dgsw.springcrmpractice.domain.Attachment;
import kr.hs.dgsw.springcrmpractice.repository.AttachRepository;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;

@Service
public class AttachServiceImpl implements AttachService {

    private final AttachRepository attachRepository;

    @Autowired
    public AttachServiceImpl(AttachRepository attachRepository) {
        this.attachRepository = attachRepository;
    }

    @Value("${spring.jpa.hibernate.ddl-auto}")
    private String DDLMode;

    @PostConstruct
    private void init() throws IOException {
        if (DDLMode.equals("create") || DDLMode.equals("create-drop")) {
            deleteFiles(new File(System.getProperty("user.dir") + "\\upload\\"));
        }
    }

    private boolean deleteFiles(File file) {
        File[] flist = null;

        if (file == null) {
            return false;
        }

        if (file.isFile()) {
            return file.delete();
        }

        if (!file.isDirectory()) {
            return false;
        }

        flist = file.listFiles();

        if (flist != null && flist.length > 0) {
            for (File f: flist) {
                if (!deleteFiles(f)) {
                    return false;
                }
            }
        }

        return file.delete();
    }

    @Override
    public Attachment attach(MultipartFile file) {
        UUID uuid = UUID.randomUUID();
        String path = "\\upload\\" + uuid.toString();

        try {
            File dest = new File(System.getProperty("user.dir") + path);
            dest.getParentFile().mkdirs();
            file.transferTo(dest);
        }
        catch (IOException e) {
            return null;
        }

        return attachRepository.save(new Attachment(uuid, path, file.getOriginalFilename()));
    }

    @Override
    public ResponseEntity<Resource> serve(String id) {
        Optional<Attachment> target = attachRepository.findById(UUID.fromString(id));
        if (!target.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Attachment targetFile = target.get();

        try {
            File file = new File(System.getProperty("user.dir") + targetFile.getPath());
            Path path = Paths.get(file.getPath());
            Resource body = new UrlResource(path.toUri());
            if (body.exists() || body.isReadable()) {
                return ResponseEntity
                        .ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + targetFile.getOrigin() + "\"")
                        .contentLength(file.length())
                        .body(body);
            }
            else {
                throw new Exception();
            }
        }
        catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    /*
    public void serve2(HttpServletRequest request, HttpServletResponse response) {
        String filePath = "a";

        File file = new File(filePath);
        if (!file.exists()) {
            return;
        }

        String mimeType = URLConnection.guessContentTypeFromName(file.getName());
        if (mimeType == null) {
            mimeType = "application/octet-stream";
        }

        response.setContentType(mimeType);
        response.setHeader("Content-Disposition", "inline; filename=\"" + file.getName() + "\"");
        response.setContentLength((int) file.length());

        try {
            InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
            FileCopyUtils.copy(inputStream, response.getOutputStream());
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
    */
}
