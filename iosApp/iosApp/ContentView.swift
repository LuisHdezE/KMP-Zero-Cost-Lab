import Foundation
import SwiftUI
import Shared

@MainActor
final class ProductsViewModel: ObservableObject {
    @Published private(set) var products: [Product] = []

    private let controller: ProductController
    private var started = false

    init() {
        controller = IosProductControllerKt.createIosProductController()
    }

    func start() {
        guard !started else { return }
        started = true

        controller.start { [weak self] snapshot in
            DispatchQueue.main.async {
                guard let self else { return }

                var nextProducts: [Product] = []
                let count = Int(snapshot.count)
                if count > 0 {
                    for index in 0..<count {
                        nextProducts.append(
                            snapshot.productAt(index: Int32(index))
                        )
                    }
                }
                self.products = nextProducts
            }
        }
    }

    func stop() {
        started = false
        controller.stop()
    }

    func create(name: String, quantity: Int, priceCents: Int64) {
        controller.createProduct(
            name: name,
            quantity: Int32(quantity),
            priceCents: priceCents
        )
    }

    func update(id: Int64, name: String, quantity: Int, priceCents: Int64) {
        controller.updateProduct(
            id: id,
            name: name,
            quantity: Int32(quantity),
            priceCents: priceCents
        )
    }

    func delete(id: Int64) {
        controller.deleteProduct(id: id)
    }
}

struct ProductDraft: Identifiable {
    let id = UUID()
    let productId: Int64?
    var name: String
    var quantity: String
    var priceCents: String

    static var empty: ProductDraft {
        ProductDraft(
            productId: nil,
            name: "",
            quantity: "1",
            priceCents: "100"
        )
    }

    static func editing(_ product: Product) -> ProductDraft {
        ProductDraft(
            productId: product.id,
            name: product.name,
            quantity: String(product.quantity),
            priceCents: String(product.priceCents)
        )
    }
}

struct ContentView: View {
    @StateObject private var model = ProductsViewModel()
    @State private var draft: ProductDraft?

    var body: some View {
        NavigationStack {
            List {
                Section {
                    ForEach(model.products, id: \.id) { product in
                        ProductRow(product: product)
                            .contentShape(Rectangle())
                            .accessibilityElement(children: .contain)
                            .accessibilityIdentifier("qa-product-row")
                            .swipeActions(edge: .leading, allowsFullSwipe: false) {
                                Button {
                                    draft = .editing(product)
                                } label: {
                                    Label("Edit", systemImage: "pencil")
                                }
                                .accessibilityIdentifier("qa-edit-product")
                                .tint(.blue)
                            }
                            .swipeActions(edge: .trailing) {
                                Button(role: .destructive) {
                                    model.delete(id: product.id)
                                } label: {
                                    Label("Delete", systemImage: "trash")
                                }
                                .accessibilityIdentifier("qa-delete-product")
                            }
                    }
                } header: {
                    Text("\(model.products.count) products")
                        .accessibilityIdentifier("qa-product-count")
                } footer: {
                    Text("Stored locally with the shared KMP Room/SQLite core.")
                }
            }
            .listStyle(.insetGrouped)
            .navigationTitle("Products")
            .toolbar {
                ToolbarItem(placement: .primaryAction) {
                    Button {
                        draft = .empty
                    } label: {
                        Image(systemName: "plus")
                    }
                    .accessibilityLabel("Add product")
                    .accessibilityIdentifier("qa-add-product")
                }
            }
            .overlay {
                if model.products.isEmpty {
                    VStack(spacing: 12) {
                        Image(systemName: "shippingbox")
                            .font(.system(size: 42))
                            .foregroundStyle(.secondary)
                        Text("No Products")
                            .font(.title3.weight(.semibold))
                        Text("Tap + to create your first product.")
                            .font(.subheadline)
                            .foregroundStyle(.secondary)
                    }
                    .multilineTextAlignment(.center)
                    .padding()
                }
            }
        }
        .sheet(item: $draft) { currentDraft in
            ProductEditorView(draft: currentDraft) { savedDraft in
                let quantity = Int(savedDraft.quantity) ?? 0
                let priceCents = Int64(savedDraft.priceCents) ?? 0

                if let id = savedDraft.productId {
                    model.update(
                        id: id,
                        name: savedDraft.name,
                        quantity: quantity,
                        priceCents: priceCents
                    )
                } else {
                    model.create(
                        name: savedDraft.name,
                        quantity: quantity,
                        priceCents: priceCents
                    )
                }
                draft = nil
            }
        }
        .onAppear {
            model.start()
        }
        .onDisappear {
            model.stop()
        }
    }
}

private struct ProductRow: View {
    let product: Product

    var body: some View {
        VStack(alignment: .leading, spacing: 6) {
            Text(product.name)
                .font(.headline)

            HStack(spacing: 14) {
                Label("\(product.quantity)", systemImage: "shippingbox")
                Text(formatPrice(product.priceCents))
                    .fontWeight(.medium)
            }
            .font(.subheadline)
            .foregroundStyle(.secondary)
        }
        .padding(.vertical, 4)
    }
}

private struct ProductEditorView: View {
    enum Field: Hashable {
        case name
        case quantity
        case price
    }

    @Environment(\.dismiss) private var dismiss
    @FocusState private var focusedField: Field?

    @State private var draft: ProductDraft
    let onSave: (ProductDraft) -> Void

    init(draft: ProductDraft, onSave: @escaping (ProductDraft) -> Void) {
        _draft = State(initialValue: draft)
        self.onSave = onSave
    }

    var body: some View {
        NavigationStack {
            Form {
                Section("Product") {
                    TextField("Name", text: $draft.name)
                        .accessibilityIdentifier("qa-name-input")
                        .textInputAutocapitalization(.words)
                        .submitLabel(.next)
                        .focused($focusedField, equals: .name)
                        .onSubmit {
                            focusedField = .quantity
                        }

                    TextField("Quantity", text: $draft.quantity)
                        .accessibilityIdentifier("qa-quantity-input")
                        .keyboardType(.numberPad)
                        .focused($focusedField, equals: .quantity)

                    TextField("Price in cents", text: $draft.priceCents)
                        .accessibilityIdentifier("qa-price-input")
                        .keyboardType(.numberPad)
                        .focused($focusedField, equals: .price)
                }

                Section {
                    HStack {
                        Text("Preview")
                        Spacer()
                        Text(pricePreview)
                            .foregroundStyle(.secondary)
                    }
                }
            }
            .navigationTitle(draft.productId == nil ? "New Product" : "Edit Product")
            .navigationBarTitleDisplayMode(.inline)
            .toolbar {
                ToolbarItem(placement: .cancellationAction) {
                    Button("Cancel") {
                        dismiss()
                    }
                }

                ToolbarItem(placement: .confirmationAction) {
                    Button("Save") {
                        focusedField = nil
                        onSave(draft)
                        dismiss()
                    }
                    .accessibilityIdentifier("qa-save-product")
                    .disabled(draft.name.trimmingCharacters(in: .whitespacesAndNewlines).isEmpty)
                }

                ToolbarItemGroup(placement: .keyboard) {
                    Spacer()
                    Button("Done") {
                        focusedField = nil
                    }
                }
            }
        }
    }

    private var pricePreview: String {
        guard let cents = Int64(draft.priceCents) else { return "$0.00" }
        return formatPrice(cents)
    }
}

private func formatPrice(_ priceCents: Int64) -> String {
    String(format: "$%.2f", Double(priceCents) / 100.0)
}
