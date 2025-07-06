describe('Promocode form testen', () => {
    beforeEach(() => {
        cy.intercept('GET', '/api/products', { fixture: '/products.json' }).as('productsRequest');
        cy.intercept('GET', '/api/categories', { fixture: '/categories.json' }).as('categoriesRequest');
        cy.intercept('POST', '/api/promocodes', { fixture: '/promocode.json' }).as('promocodeRequest');
        cy.loginAsSuperAdmin();
        cy.visit('/promocodes/new');
    });

    //!!! zet voor elke .type() een .focus() ervoor, anders werkt het niet in de test.
    // Dit is een workaround voor het probleem dat de input niet gefocust kan worden door de fixed header.

    it('Toont validatiefout bij lege submit', () => {
        cy.get('[name="promocodeInput"]').focus().blur(); // Focus and blur to trigger validation
        cy.get('mat-error').should('contain', 'Promo code is required'); // or the translated error text
    });

    it('Wisselt tussen CATEGORY en PRODUCT, na invullen test promocode', () => {

        //multiple methods to focus the input field,
        //because header is fixed and has a higher z-index.

        // Prefer focusing the input directly
        cy.get('[name="promocodeInput"]').focus().type('TEST123');

        // Or click the form field container
        //         cy.get('[name="promocodeInput"]').parents('mat-form-field').click();
        //         cy.get('[name="promocodeInput"]').type('TEST123');

        // cy.get('.Nav-bar').invoke('css', 'z-index', '0');
        // cy.get('[name="promocodeInput"]').click().type('TEST123');
        // Optionally, restore the z-index after
        // cy.get('.Nav-bar').invoke('css', 'z-index', '1000');
        cy.get('[name="promocodeInput"]').should('have.value', 'TEST123');
        cy.get('[name="categoryScopeTypeBtn"]').scrollIntoView().click();
        cy.wait('@categoriesRequest');
        cy.get('[name="scopeValue"]').should('exist');
        cy.clearCacheStorage();

        cy.get('[name="productScopeTypeBtn"]').scrollIntoView().click();
        cy.wait('@productsRequest');
        cy.get('[name="scopeValue"]').should('exist');
        cy.clearCacheStorage();

    });

    it('Wisselt tussen percentage en vaste korting', () => {
        // cy.wait('@loginRequest');

        cy.get('[name="percentageButton"]').click();
        cy.get('[name="discountValuePercentage"]').should('exist');

        cy.get('[name="fixedValueButton"]').click();
        cy.get('[name="discountValueFixed"]').should('exist');
    });

    it('Verstuurt formulier met geldige data', () => {

        // Prefer focusing the input directly
        cy.get('[name="promocodeInput"]').focus().type('TEST123');
        cy.get('[name="promocodeInput"]').should('have.value', 'TEST123');

        cy.get('[name="categoryScopeTypeBtn"]').click();
        cy.get('[name="scopeValue"]').should('exist');
        cy.wait('@categoriesRequest');
        cy.get('[name="scopeValue"]').click();
        cy.get('[name="scopeValue"]').should('exist');
        // Select the first category option
        cy.get('mat-option').first().click();
        cy.clearCacheStorage();

        cy.get('[name="percentageButton"]').click();
        cy.get('[name="discountValuePercentage"]').focus().type('50');
        cy.get('[name="discountValuePercentage"]').should('have.value', '50');
        cy.get('[name="minOrderAmount"]').focus().type('1000');
        cy.get('[name="minOrderAmount"]').should('have.value', '1000');
        cy.get('[name="maxUsesPerEmail"]').focus().type('3');
        cy.get('[name="maxUsesPerEmail"]').should('have.value', '3');
        cy.get('[name="expiryDate"]').focus().type('2025-06-19');
        cy.get('[name="expiryDate"]').should('have.value', '2025-06-19');

        cy.get('[name="submit"]').click();

        cy.wait('@promocodeRequest').its('response.statusCode').should('eq', 200);
        cy.url().should('include', '/admin')
    });

    it('Annuleert formulier en reset velden', () => {
        cy.get('[name="promocodeInput"]').focus().type('TEST123');
        cy.get('[name="promocodeInput"]').should('have.value', 'TEST123');

        cy.get('[name="return"]').click();

        cy.url().should('include', '/admin');
    });
});
