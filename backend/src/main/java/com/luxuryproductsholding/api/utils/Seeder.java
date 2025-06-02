package com.luxuryproductsholding.api.utils;

import com.luxuryproductsholding.api.DAO.CategoryRepository;
import com.luxuryproductsholding.api.DAO.ProductRepository;
import com.luxuryproductsholding.api.DAO.RoleRepository;
import com.luxuryproductsholding.api.DAO.UserRepository;
import com.luxuryproductsholding.api.models.*;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class Seeder {

    private ProductRepository productRepository;
    private CategoryRepository categoryRepository;
    private UserRepository userRepository;
    private RoleRepository roleRepository;

    public Seeder(ProductRepository productRepository, CategoryRepository categoryRepository,
                  UserRepository userRepository, RoleRepository roleRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @EventListener
    public void seed(ContextRefreshedEvent event) {
        seedCategoriesAndProducts();
        seedRoles();
        seedAdminUser();
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

    private void seedRoles() {
        // Voeg unieke rollen toe
        roleRepository.save(new Role("ROLE_CEO"));
        roleRepository.save(new Role("ROLE_CFO"));
        roleRepository.save(new Role("ROLE_ECOMMERCE"));
        roleRepository.save(new Role("ROLE_MARKETING"));
        roleRepository.save(new Role("ROLE_IT_DEV"));
        roleRepository.save(new Role("ROLE_KLANTENSERVICE"));
        roleRepository.save(new Role("ROLE_JURIDISCH"));

        // Voeg de algemene 'Admin' rol toe
        // admin, heeft op z'n minst toegang tot admin paneel op frontend.
        // SuperAdmin, heeft toegang tot alle admin functies op frontend.

        // exotische rollen:
        // All_insights, heeft toegang tot alle insights pagina's op frontend.
        // Make_and_deactivate_promocodes, heeft toegang tot de pagina om promotiecodes te maken en te deactiveren.
        // Insight_promocode_usage, heeft toegang tot de pagina om het gebruik van promotiecodes te bekijken.
        roleRepository.save(new Role("Admin"));
        roleRepository.save(new Role("SuperAdmin"));
        roleRepository.save(new Role("All_insights"));
        roleRepository.save(new Role("Make_and_deactivate_promocodes"));
        roleRepository.save(new Role("Insight_promocode_usage"));
    }

    public void seedAdminUser() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        CustomUser ceo = new CustomUser("eijckdom.r@luxuryproductsholding.com", encoder.encode("FaultyPassword!123"));
        ceo.getRoles().add(roleRepository.findByName("Admin"));
        ceo.getRoles().add(roleRepository.findByName("SuperAdmin"));
        ceo.getRoles().add(roleRepository.findByName("ROLE_CEO"));
        userRepository.save(ceo);

        CustomUser cfo = new CustomUser("koers.s@luxuryproductsholding.com", encoder.encode("Password1234#"));
        cfo.getRoles().add(roleRepository.findByName("Admin"));
        cfo.getRoles().add(roleRepository.findByName("All_insights"));
        cfo.getRoles().add(roleRepository.findByName("ROLE_CFO"));
        userRepository.save(cfo);

        CustomUser ecommerce = new CustomUser("visser.c@luxuryproductsholding.com", encoder.encode("Password1235#"));
        ecommerce.getRoles().add(roleRepository.findByName("Admin"));
        ecommerce.getRoles().add(roleRepository.findByName("Make_and_deactivate_promocodes"));
        ecommerce.getRoles().add(roleRepository.findByName("ROLE_ECOMMERCE"));
        userRepository.save(ecommerce);

        CustomUser marketing = new CustomUser("mulder.j@luxuryproductsholding.com", encoder.encode("Password1236#"));
        marketing.getRoles().add(roleRepository.findByName("Admin"));
        marketing.getRoles().add(roleRepository.findByName("Insight_promocode_usage"));
        marketing.getRoles().add(roleRepository.findByName("ROLE_MARKETING"));
        userRepository.save(marketing);

        CustomUser it = new CustomUser("hermans.e@luxuryproductsholding.com", encoder.encode("Password1237#"));
        it.getRoles().add(roleRepository.findByName("Admin"));
        it.getRoles().add(roleRepository.findByName("SuperAdmin"));
        it.getRoles().add(roleRepository.findByName("ROLE_IT_DEV"));
        userRepository.save(it);

        CustomUser klantenservice = new CustomUser("jansen.s@luxuryproductsholding.com", encoder.encode("Password1238#"));
        klantenservice.getRoles().add(roleRepository.findByName("Admin"));
        klantenservice.getRoles().add(roleRepository.findByName("Insight_promocode_usage"));
        klantenservice.getRoles().add(roleRepository.findByName("ROLE_KLANTENSERVICE"));
        userRepository.save(klantenservice);

        CustomUser juridisch = new CustomUser("karim.n@luxuryproductsholding.com", encoder.encode("Password1239#"));
        juridisch.getRoles().add(roleRepository.findByName("Admin"));
        juridisch.getRoles().add(roleRepository.findByName("Insight_promocode_usage"));
        juridisch.getRoles().add(roleRepository.findByName("ROLE_JURIDISCH"));
        userRepository.save(juridisch);
    }
}
