package br.com.eicon.rest;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import br.com.eicon.domain.PurchaseOrder;
import br.com.eicon.service.PurchaseOrderService;

/**
 * Serviços REST para o consumo
 * @author Douglas-Takara
 */
@RestController
@RequestMapping("/pedidos")
public class PurchaseOrderWS {

	@Autowired
	private PurchaseOrderService service;
	
	@RequestMapping(value="/json", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	public List<PurchaseOrder> getPedidoJson() {
		return service.findAll();
	}

	@RequestMapping(value="/xml", method=RequestMethod.GET, produces=MediaType.APPLICATION_XML_VALUE)
	public List<PurchaseOrder> getPedidoXml() {
		return service.findAll();
	}
	
	/**
	 * consulta de acordo com o numero de controle e a data de cadastro
	 * @param pedidoId
	 * @param dtCadastro - dd-MM-yyyy
	 * @return
	 */
	@RequestMapping(value="/xml/{pedidoId}/{dataCadastro}", method=RequestMethod.GET, produces=MediaType.APPLICATION_XML_VALUE)
	public List<PurchaseOrder> getPedidoXml(@PathVariable("pedidoId") Long pedidoId, @PathVariable("dataCadastro") @DateTimeFormat(iso=ISO.DATE) String dtCadastro) {
		LocalDate date = LocalDate.parse(dtCadastro, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
		return service.findByControlIdAndRegisterDate(pedidoId, date);
	}
	
	@RequestMapping(value="/json/{pedidoId}/{dataCadastro}", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	public List<PurchaseOrder> getPedidoJson(@PathVariable("pedidoId") Long pedidoId, @PathVariable("dataCadastro") @DateTimeFormat(iso=ISO.DATE) String dtCadastro) {
		LocalDate date = LocalDate.parse(dtCadastro, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
		return service.findByControlIdAndRegisterDate(pedidoId, date);
	}

	@RequestMapping(value="/upload/xml", method=RequestMethod.POST)
	public @ResponseBody String uploadXmlFile(@RequestParam("file") MultipartFile multipartFile) throws IOException {
		File file = createFile(multipartFile);
		if(!multipartFile.isEmpty()) {
			try {
				XmlMapper mapper = new XmlMapper();
				String xml = inputStreamToString(new FileInputStream(file));
				List<PurchaseOrder> pedidos = mapper.readValue(xml, new TypeReference<List<PurchaseOrder>>() {});
				return service.saveAll(pedidos);
			}catch(Exception e) {
				return "You failed to upload " + file.getName() + " => " + e.getMessage();
			}
		} else {
			return "You failed to upload " + file.getName() + " because the file was empty.";
		}
	}
	
	@RequestMapping(value="/upload/json", method=RequestMethod.POST)
	public String uploadJsonFile(@RequestParam("file") MultipartFile multipartFile) throws IOException {
		File file = createFile(multipartFile);
		if(!multipartFile.isEmpty()) {
			try {
				ObjectMapper mapper = new ObjectMapper();
				String jsonValues = inputStreamToString(new FileInputStream(file));
				List<PurchaseOrder> pedidos = mapper.readValue(jsonValues, new TypeReference<List<PurchaseOrder>>() {});
				service.saveAll(pedidos);
				return "Pedidos salvos com sucesso";
			}catch(Exception e) {
				return "You failed to upload " + file.getName() + " => " + e.getMessage();
			}
		} else {
			return "You failed to upload " + file.getName() + " because the file was empty.";
		}
	}

	private String inputStreamToString(final FileInputStream fileInputStream) throws IOException {
		StringBuilder sb = new StringBuilder();
		String line;
		BufferedReader reader = new BufferedReader(new InputStreamReader(fileInputStream));
		while((line = reader.readLine()) != null) {
			sb.append(line);
		}
		reader.close();
		return sb.toString();
	}

	private File createFile(final MultipartFile multipartFile) throws IOException {
		File file = new File(multipartFile.getOriginalFilename());
		file.createNewFile();
		FileOutputStream fos = new FileOutputStream(file);
		fos.write(multipartFile.getBytes());
		fos.close();
		return file;
	}
}