/// <reference types="cypress" />
// ***********************************************
// This example commands.ts shows you how to
// create various custom commands and overwrite
// existing commands.
//
// For more comprehensive examples of custom
// commands please read more here:
// https://on.cypress.io/custom-commands
// ***********************************************
//
//
// -- This is a parent command --
// Cypress.Commands.add('login', (email, password) => { ... })
//
//
// -- This is a child command --
// Cypress.Commands.add('drag', { prevSubject: 'element'}, (subject, options) => { ... })
//
//
// -- This is a dual command --
// Cypress.Commands.add('dismiss', { prevSubject: 'optional'}, (subject, options) => { ... })
//
//
// -- This will overwrite an existing command --
// Cypress.Commands.overwrite('visit', (originalFn, url, options) => { ... })
//
// declare global {
//   namespace Cypress {
//     interface Chainable {
//       login(email: string, password: string): Chainable<void>
//       drag(subject: string, options?: Partial<TypeOptions>): Chainable<Element>
//       dismiss(subject: string, options?: Partial<TypeOptions>): Chainable<Element>
//       visit(originalFn: CommandOriginalFn, url: string, options: Partial<VisitOptions>): Chainable<Element>
//     }
//   }
// }

declare global {
    namespace Cypress {
        interface Chainable {
            clearCacheStorage(): Chainable<void>;
            loginAsSuperAdmin(): Chainable<void>;
        }
    }
}

Cypress.Commands.add('clearCacheStorage', () => {
    cy.window().then((win) => {
        if ('caches' in win) {
            return win.caches.keys().then((keys) => {
                return Promise.all(keys.map((key) => win.caches.delete(key)));
            });
        }
        return undefined;
    });
})

Cypress.Commands.add('loginAsSuperAdmin', () => {
    cy.intercept('POST', '/api/auth/login', { fixture: '/eijckdom.json' }).as('loginRequest');
    cy.intercept('GET', '/api/products', { fixture: '/products.json' }).as('productsRequest');
    cy.intercept('GET', '/api/categories', { fixture: '/categories.json' }).as('categoriesRequest');
    cy.clearCacheStorage();
    cy.visit('/login');
    cy.get('input[name=email]').type('eijckdom.r@luxuryproductsholding.com');
    cy.get('input[name="password"]').type('FaultyPassword!123');
    cy.get('button[type="submit"]').click();
    cy.wait('@loginRequest');
    cy.wait('@productsRequest');
    cy.url().should('include', '/products');
    cy.clearCacheStorage();
});

export {};