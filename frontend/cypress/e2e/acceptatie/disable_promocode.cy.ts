describe('Promocode deactivation', () => {
    beforeEach(() => {
        cy.intercept('GET', '/api/products', {fixture: '/products.json'}).as('productsRequest');
        cy.intercept('GET', '/api/categories', {fixture: '/categories.json'}).as('categoriesRequest');
        cy.intercept('POST', '/api/promocodes', {fixture: '/promocode.json'}).as('promocodeRequest');
        cy.intercept('GET', '/api/promocodes', {fixture: '/promocodes_list.json'}).as('promocodescodesRequest');
        cy.intercept('PUT','/api/promocodes/disable_promocode_*', {fixture: '/promocode_disabled_response.json'}).as('disabledResponse');
        cy.loginAsSuperAdmin();
        cy.visit('/promocode_beheer/deactivate_promocodes');
        cy.wait(1000); // Brief pause to ensure page stability
    });

    it('Kan promocode selecteren uit dropdown list', () => {
        cy.wait('@promocodescodesRequest');

        // Ensure API response has data
        cy.get('@promocodescodesRequest').its('response.body').should('have.length.greaterThan', 0);

        cy.get('form').within(() => {
            // Click mat-select trigger and wait for it to be ready
            cy.get('mat-select[formControlName="promocode"]')
                .should('be.visible')
                .should('not.be.disabled')
                .click({ force: true });
        });

        // Wait for CDK overlay to appear
        cy.get('.cdk-overlay-pane', { timeout: 20000 })
            .should('be.visible');

        // Target first mat-option in overlay
        cy.get('mat-option', { timeout: 20000 })
            .should('be.visible')
            .first()
            .click({ force: true });

        cy.get('form').within(() => {
            // Submit form
            cy.get('[name="submit"]').click();
        });

        cy.wait('@disabledResponse');
    });

})