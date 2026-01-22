describe('Home Page', () => {
  it('should load the home page', () => {
    cy.visit('http://localhost:4200/');
    cy.url().should('include', '/');
  });
});
