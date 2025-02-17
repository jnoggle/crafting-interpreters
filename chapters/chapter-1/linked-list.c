#include <stdio.h>
#include <stdlib.h>
#include <string.h>

// Node structure for the doubly linked list
typedef struct Node {
    char *data;
    struct Node *prev, *next;
} Node;

// Function to create a new node
Node *create_node(const char *str) {
    Node *new_node = (Node *)malloc(sizeof(Node));
    if (!new_node) {
        perror("Failed to allocate memory");
        exit(EXIT_FAILURE);
    }
    new_node->data = strdup(str);
    if (!new_node->data) {
        perror("Failed to allocate memory for string");
        free(new_node);
        exit(EXIT_FAILURE);
    }
    new_node->prev = new_node->next = NULL;
    return new_node;
}

// Function to insert a node at the end
void insert(Node **head, const char *str) {
    Node *new_node = create_node(str);
    if (!*head) {
        *head = new_node;
        return;
    }
    Node *temp = *head;
    while (temp->next) {
        temp = temp->next;
    }
    temp->next = new_node;
    new_node->prev = temp;
}

// Function to find a node by string
Node *find(Node *head, const char *str) {
    while (head) {
        if (strcmp(head->data, str) == 0) {
            return head;
        }
        head = head->next;
    }
    return NULL;
}

// Function to delete a node
void delete(Node **head, const char *str) {
    Node *target = find(*head, str);
    if (!target) return;
    
    if (target->prev) {
        target->prev->next = target->next;
    } else {
        *head = target->next;
    }
    if (target->next) {
        target->next->prev = target->prev;
    }
    
    free(target->data);
    free(target);
}

// Function to print the list
void print_list(Node *head) {
    while (head) {
        printf("%s <-> ", head->data);
        head = head->next;
    }
    printf("NULL\n");
}

// Function to free the list
void free_list(Node *head) {
    while (head) {
        Node *temp = head;
        head = head->next;
        free(temp->data);
        free(temp);
    }
}

int main() {
    Node *head = NULL;
    
    // Test insertions
    insert(&head, "Alice");
    insert(&head, "Bob");
    insert(&head, "Charlie");
    print_list(head);
    
    // Test find
    Node *found = find(head, "Bob");
    printf("Found: %s\n", found ? found->data : "Not found");
    
    // Test deletion
    delete(&head, "Bob");
    print_list(head);
    
    // Free memory
    free_list(head);
    return 0;
}
