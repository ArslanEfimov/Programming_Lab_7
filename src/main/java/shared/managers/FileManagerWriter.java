//package shared.managers;
//
//import shared.io.UserIO;
//
//import javax.xml.bind.JAXBContext;
//import javax.xml.bind.JAXBException;
//import javax.xml.bind.Marshaller;
//import java.io.*;
//import java.nio.file.Paths;
//
///**
// * class for serializing data from a file
// */
//public class FileManagerWriter {
//    private CollectionManager collectionManager;
//    private UserIO userIO;
//    private FileManagerReader fileManagerReader;
//
//    public FileManagerWriter(CollectionManager collectionManager, FileManagerReader fileManagerReader) {
//        this.collectionManager = collectionManager;
//        this.userIO = new UserIO();
//        this.fileManagerReader = fileManagerReader;
//    }
//
//    /**
//     * method converts data from collection to file
//     * @param collectionManager
//     * @throws FileNotFoundException
//     * @exception JAXBException
//     */
//    public void writerInFile(CollectionManager collectionManager) throws FileNotFoundException {
//        try {
//            JAXBContext jaxbContext = JAXBContext.newInstance(CollectionManager.class);
//            Marshaller marshaller = jaxbContext.createMarshaller();
//            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
//            marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
//            PrintWriter printWriter;
//            if(!(Paths.get(fileManagerReader.getFileName()).toFile().exists())){
//                userIO.println("файл не найден, предложите создать новый файл");
//                userIO.print("введите имя для нового файла (без .xml): ");
//                String newFileName = userIO.readLine().trim()+".xml";
//                printWriter = new PrintWriter(newFileName);
//
//            }
//            else {
//                printWriter = new PrintWriter(fileManagerReader.getFileName());
//            }
//            marshaller.marshal(collectionManager,printWriter);
//            printWriter.close();
//
//        }catch (JAXBException ex){
//            userIO.println("внезапная ошибка при попытке записи в файл");
//        }catch (IOException ex){
//            userIO.println("не удается записать данные в файл, скорее всего, у вас нет прав на запись в файл");
//        }
//    }
//}