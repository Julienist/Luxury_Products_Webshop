package com.luxuryproductsholding.api.utils;

import com.luxuryproductsholding.api.DAO.CategoryRepository;
import com.luxuryproductsholding.api.DAO.ProductRepository;
import com.luxuryproductsholding.api.DAO.UserRepository;
import com.luxuryproductsholding.api.models.Category;
import com.luxuryproductsholding.api.models.CustomUser;
import com.luxuryproductsholding.api.models.Product;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class Seeder {

    private ProductRepository productRepository;
    private CategoryRepository categoryRepository;
    private UserRepository userRepository;

    public Seeder(ProductRepository productRepository, CategoryRepository categoryRepository,
                  UserRepository userRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
    }

    @EventListener
    public void seed(ContextRefreshedEvent event) {
        seedCategoriesAndProducts();
    }

    private void seedCategoriesAndProducts() {
        Category category1 = new Category("Horloges");
        Category category2 = new Category("Armbanden");
        Category category3 = new Category("Tassen");
        Category category4 = new Category("Brillen");

        this.categoryRepository.save(category1);
        this.categoryRepository.save(category2);
        this.categoryRepository.save(category3);
        this.categoryRepository.save(category4);

        Product product1 = new Product("Rolex Submariner", "Iconisch duikhorloge met zwarte wijzerplaat en keramische bezel. Een tijdloos statussymbool.", 7999.99, "https://i0.wp.com/wannabuyawatch.com/wp-content/uploads/2021/03/52078.jpg?fit=960%2C1280&ssl=1", category1);
        Product product2 = new Product("Omega Speedmaster", "De legendarische Moonwatch die gedragen werd tijdens Apollo-missies. Elegant en sportief.", 6299.00, "https://www.eugenevanbaal.nl/media/catalog/product/cache/4f2c711297192d1e648fdfaf4a68b673/3/2/32930445104001.webp", category1);
        Product product3 = new Product("Cartier Love Bracelet", "Gouden armband met schroefsysteem. Symbool van eeuwige liefde en toewijding.", 7150.00, "https://a.1stdibscdn.com/archivesE/upload/j_214/15_15/org_mtsj118232/MTSJ118232_l.jpeg?disable=upscale&auto=webp&quality=60&width=1400", category2);
        Product product4 = new Product("Tiffany & Co. T Wire Bracelet", "Minimalistische armband in 18k goud met iconisch T-ontwerp. Subtiel en stijlvol.", 3450.00, "https://www.net-a-porter.com/variants/images/17957409494218415/in/w2000_q60.jpg", category2);
        Product product5 = new Product("Louis Vuitton Neverfull MM", "Luxe handtas van gecoat canvas en leer. Ruim, stijlvol en herkenbaar aan het LV-monogram.", 1650.00, "https://en.louisvuitton.com/images/is/image/lv/1/PP_VP_L/louis-vuitton-neverfull-mm--N40599_PM1_Side%20view.png?wid=2400&hei=2400", category3);
        Product product6 = new Product("Chanel Classic Flap Bag", "Elegante tas met kettingband en quilted leer. Een modeklassieker die nooit uit de mode raakt.", 7950.00, "https://images.vestiairecollective.com/images/resized/w=1024,q=75,f=auto,/produit/chanel-timeless-classique-leer-zwart-handtas-48641947-1_3.jpg", category3);
        Product product7 = new Product("Ray-Ban Aviator Gold", "De originele pilotenbril met gouden frame en groene lenzen. Tijdloos design.", 599.00, "https://grandvision-media.imgix.net/m/6167739297197404/original_png-0RB3025__001_51__STD__shad__qt.png?w=1440&auto=format", category4);
        Product product8 = new Product("Gucci GG0406S", "Statement zonnebril met goudkleurig frame en oversized lenzen. Onmiskenbaar Gucci.", 690.00, "https://grandvision-media.imgix.net/m/7a7a65e179d137dc/original_png-gucci_gg0121o_8056376076967_00025.png?w=1440&auto=format", category4);
        Product product9 = new Product("Persol PO0714 Folding", "Handgemaakte zonnebril uit Italië, bekend van Steve McQueen. Inklapbaar en stijlvol.", 520.00, "https://image4.cdnsbg.com/1/73/15835_side_1_1609226326671.jpg?width=900&height=450&q=90", category4);
        Product product10 = new Product("Tag Heuer Carrera Calibre 5", "Automatisch horloge met sportief design en Zwitserse precisie. Perfect voor dagelijks gebruik.", 2850.00, "https://chronexttime.imgix.net/V/8/V85849/V85849_1_det.png?w=570&ar=1:1&auto=format&fm=png&q=55&usm=50&usmrad=1.5&dpr=2&trim=color&fit=fill&auto=compress&bg=FFFFFF&bg-remove=true", category1);

        this.productRepository.save(product1);
        this.productRepository.save(product2);
        this.productRepository.save(product3);
        this.productRepository.save(product4);
        this.productRepository.save(product5);
        this.productRepository.save(product6);
        this.productRepository.save(product7);
        this.productRepository.save(product8);
        this.productRepository.save(product9);
        this.productRepository.save(product10);
    }
}
