import XCTest

final class ProductCrudUITests: XCTestCase {
    override func setUpWithError() throws {
        continueAfterFailure = false
    }

    func testCreatePersistEditDeleteProduct() throws {
        let app = XCUIApplication()
        app.launchArguments = [
            "-AppleLanguages", "(en)",
            "-AppleLocale", "en_US"
        ]
        app.launch()

        assertProductCount(0, in: app)

        let addButton = app.buttons["qa-add-product"]
        XCTAssertTrue(addButton.waitForExistence(timeout: 5))
        addButton.tap()

        let nameField = app.textFields["qa-name-input"]
        XCTAssertTrue(nameField.waitForExistence(timeout: 5))
        nameField.tap()
        nameField.typeText("QA Widget")

        let saveButton = app.buttons["qa-save-product"]
        XCTAssertTrue(saveButton.isEnabled)
        saveButton.tap()

        XCTAssertTrue(app.staticTexts["QA Widget"].waitForExistence(timeout: 10))
        XCTAssertTrue(app.staticTexts["$1.00"].exists)
        assertProductCount(1, in: app)

        app.terminate()
        app.launch()

        XCTAssertTrue(app.staticTexts["QA Widget"].waitForExistence(timeout: 10))
        assertProductCount(1, in: app)

        let row = app.otherElements["qa-product-row"]
        XCTAssertTrue(row.waitForExistence(timeout: 5))
        row.swipeRight()

        let editButton = app.buttons["qa-edit-product"]
        XCTAssertTrue(editButton.waitForExistence(timeout: 5))
        editButton.tap()

        let editName = app.textFields["qa-name-input"]
        XCTAssertTrue(editName.waitForExistence(timeout: 5))
        editName.coordinate(withNormalizedOffset: CGVector(dx: 0.95, dy: 0.5)).tap()
        editName.typeText(" Updated")
        app.buttons["qa-save-product"].tap()

        XCTAssertTrue(app.staticTexts["QA Widget Updated"].waitForExistence(timeout: 10))

        let updatedRow = app.otherElements["qa-product-row"]
        XCTAssertTrue(updatedRow.waitForExistence(timeout: 5))
        updatedRow.swipeLeft()

        let deleteButton = app.buttons["qa-delete-product"]
        XCTAssertTrue(deleteButton.waitForExistence(timeout: 5))
        deleteButton.tap()

        XCTAssertTrue(app.staticTexts["No Products"].waitForExistence(timeout: 10))
        assertProductCount(0, in: app)
        XCTAssertFalse(app.staticTexts["QA Widget Updated"].exists)
    }

    private func assertProductCount(
        _ expected: Int,
        in app: XCUIApplication,
        file: StaticString = #filePath,
        line: UInt = #line
    ) {
        let count = app.staticTexts["qa-product-count"]
        XCTAssertTrue(count.waitForExistence(timeout: 10), file: file, line: line)
        XCTAssertEqual(
            count.label.lowercased(),
            "\(expected) products",
            file: file,
            line: line
        )
    }
}
