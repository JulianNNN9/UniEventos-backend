package co.edu.uniquindio.unieventos.services.implementacion;

import co.edu.uniquindio.unieventos.services.interfaces.ImagenesService;
import com.google.cloud.storage.*;
import com.google.firebase.cloud.StorageClient;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.UUID;

@Service
public class ImagenesServiceImple implements ImagenesService {

    @Override
    public String subirImagen(MultipartFile multipartFile) throws Exception{
        Bucket bucket = StorageClient.getInstance().bucket();
        String fileName = String.format( "%s-%s", UUID.randomUUID(), multipartFile.getOriginalFilename() );
        Blob blob = bucket.create( fileName, multipartFile.getInputStream(), multipartFile.getContentType() );


        return String.format(
                "https://firebasestorage.googleapis.com/v0/b/%s/o/%s?alt=media",
                bucket.getName(),
                blob.getName()
        );
    }

    @Override
    public void eliminarImagen(String urlImagen) throws Exception {

        // Extraer el nombre del archivo de la URL de Firebase Storage
        String nombreImagen = obtenerNombreImagenDeURL(urlImagen);

        // Obtener el bucket de Firebase Storage
        Bucket bucket = StorageClient.getInstance().bucket();

        // Buscar el archivo en el bucket
        Blob blob = bucket.get(nombreImagen);

        // Verificar si el archivo existe
        if (blob == null) {
            throw new Exception("No se encontró la imagen con el nombre: " + nombreImagen);
        }

        // Intentar eliminar el archivo
        boolean deleted = blob.delete();
        if (!deleted) {
            throw new Exception("No se pudo eliminar la imagen: " + nombreImagen);
        }
    }
    private String obtenerNombreImagenDeURL(String urlImagen) {
        String nombreImagen = urlImagen.substring(urlImagen.indexOf("o/") + 2, urlImagen.indexOf("?alt=media"));
        return nombreImagen;
    }
}

