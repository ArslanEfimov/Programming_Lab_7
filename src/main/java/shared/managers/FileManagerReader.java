//package shared.managers;
//
//
//import shared.core.models.Organization;
//
//import javax.xml.bind.JAXBContext;
//import javax.xml.bind.JAXBException;
//import javax.xml.bind.Unmarshaller;
//import java.io.File;
//import java.nio.file.Paths;
//import java.util.Vector;
//
//public class FileManagerReader {
//    private CollectionManager collectionManager;
//    private final String fileName;
//
//    public FileManagerReader(String fileName) {
//        collectionManager = new CollectionManager();
//        this.fileName = fileName;
//    }
//
//    public Vector<Organization> readCollection() {
//        try {
//            File file = new File(fileName);
//            JAXBContext context = JAXBContext.newInstance(CollectionManager.class);
//            Unmarshaller unmarshaller = context.createUnmarshaller();
//            collectionManager = (CollectionManager) unmarshaller.unmarshal(file);
//            collectionManager.checkCollection(collectionManager.getCollectionVector());
//            return collectionManager.getCollectionVector();
//        }  catch (JAXBException ex) {
//            System.err.println("файл поврежден, проверьте его");
//            return new Vector<>();
//        } catch (IllegalArgumentException ex) {
//            if (Paths.get(fileName).toFile().exists()) {
//                System.err.println("файл не может быть прочитан, проверьте разрешение");
//            }
//            else {
//                System.err.println("файл не найден, проверьте имя файла");
//            }
//            return new Vector<>();
//        }
//    }
//    public String getFileName () {
//        return fileName;
//    }
//}
