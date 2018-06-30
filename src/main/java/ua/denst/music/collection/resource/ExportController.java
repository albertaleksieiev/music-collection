package ua.denst.music.collection.resource;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.denst.music.collection.service.FileSystemExportService;

@AllArgsConstructor
@RestController
@CrossOrigin
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequestMapping(value = "/api/export")
public class ExportController {

    FileSystemExportService fileSystemExportService;

    @PostMapping(params = {"collectionName", "createCopyInEveryGenreFolder"})
    public ResponseEntity<Integer> create(@RequestParam(name = "collectionName") final String collectionName,
                                          @RequestParam(name = "createCopyInEveryGenreFolder", defaultValue = "false") final boolean createCopyInEveryGenreFolder) {
        final Integer countImported = fileSystemExportService.exportCollection(collectionName, createCopyInEveryGenreFolder);
        return new ResponseEntity<>(countImported, HttpStatus.OK);
    }
}
