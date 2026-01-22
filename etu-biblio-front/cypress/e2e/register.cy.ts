describe('Register Page', () => {
  it('should register a new user', () => {
    cy.visit('http://localhost:4200/register');

    cy.intercept('POST', '/api/register', {
      statusCode: 200,
      body: { success: true }
    }).as('register');

    cy.get('input[formControlName="firstName"]').type('John');
    cy.get('input[formControlName="lastName"]').type('Doe');
    cy.get('input[formControlName="login"]').type('john');
    cy.get('input[formControlName="password"]').type('1234');

    cy.get('[data-cy="register-submit"]').click();

    cy.wait('@register');
  });
});
