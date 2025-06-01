describe('login goes correct with a SuperAdmin user', () => {
    beforeEach(() => {
        cy.visit('/login');
        cy.intercept('POST', '/api/auth/login', { fixture: '/eijckdom.json' }).as('loginRequest');
        cy.intercept('GET', '/api/products', { fixture: '/products.json' }).as('productsRequest');
    });

    it('should login with SuperAdmin credentials', () => {
        cy.clearCacheStorage();
        cy.get('input[name=email]').type('eijckdom.r@luxuryproductsholding.com');
        cy.get('input[name="password"]').type('FaultyPassword!123');
        cy.get('button[type="submit"]').click();

        cy.wait('@loginRequest');
        cy.wait('@productsRequest');
        cy.url().should('include', '/products');

    });
});