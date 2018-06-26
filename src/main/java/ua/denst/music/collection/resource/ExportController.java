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

    @PostMapping(params = {"collectionName", "rootFolder", "bitRate"})
    public ResponseEntity<Integer> create(@RequestParam(name = "collectionName") final String collectionName,
                                        @RequestParam(name = "rootFolder") final String rootFolder,
                                        @RequestParam(name = "bitRate") final Short bitRate) {
        final Integer countSynced = fileSystemExportService.syncCollectionFromFileSystem(collectionName, rootFolder, bitRate);

        return new ResponseEntity<>(countSynced, HttpStatus.OK);
    }
}
